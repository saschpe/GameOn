package saschpe.gameon.mobile.game.overview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.Module.colors
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Analytics
import saschpe.gameon.mobile.base.customtabs.openUrl
import saschpe.gameon.mobile.databinding.FragmentGameOverviewBinding
import saschpe.gameon.mobile.game.GameFragment

class GameOverviewFragment : Fragment() {
    private lateinit var argPlain: String
    private val viewModel: GameOverviewViewModel by viewModels()
    private var priceAlertTextWatcher = object : TextWatcher {
        private var currentJob: Job? = null

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        override fun afterTextChanged(text: Editable?) {
            updatePriceAlertStartIcon()

            currentJob?.cancel() // Only have one, i.e. the latest, running...
            currentJob = lifecycleScope.launch {
                delay(200L) // Rate-limit to avoid an update on every key press...
                viewModel.favoriteLiveData.value?.let { result ->
                    if (result is Result.Success<Favorite>) {
                        var priceAlertString = text?.toString()
                        if (priceAlertString?.isBlank() == true) {
                            priceAlertString = null
                        }
                        firebaseAnalytics.logEvent(Analytics.Event.UPDATE_ON_WISHLIST) {
                            param(FirebaseAnalytics.Param.ITEM_ID, argPlain)
                            priceAlertString?.let { price ->
                                param(FirebaseAnalytics.Param.PRICE, price)
                            }
                        }
                        viewModel.updateFavorite(result.data.copy(priceThreshold = priceAlertString?.toDouble()))
                    }
                }
            }
        }
    }
    private lateinit var binding: FragmentGameOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argPlain = requireNotNull(arguments?.getString(ARG_PLAIN))
        viewModel.getGameInfo(argPlain)
        viewModel.getGameOverview(argPlain)
        viewModel.getFavorite(argPlain)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.gameInfoLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success<GameInfo> -> binding.cover.load(result.data.image) {
                    placeholder(R.drawable.placeholder)
                    crossfade(true)
                }
                is Result.Error -> {
                    result.errorLogged()
                    parentFragment?.let {
                        if (it is GameFragment) {
                            it.showSnackBarWithRetryAction(R.string.unable_to_load_game) {
                                viewModel.getGameInfo(argPlain)
                            }
                        }
                    }
                }
            }
        })

        viewModel.gameOverviewLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success<GameOverview> -> {
                    if (result.data.price != null) {
                        result.data.price?.run {
                            val priceString = if (cut == 0) {
                                getString(
                                    R.string.price_on_store_colored_template,
                                    price_formatted, store, colors.green
                                )
                            } else {
                                getString(
                                    R.string.price_on_store_with_rebate_template,
                                    price_formatted, store, cut, colors.green, colors.red
                                )
                            }
                            binding.currentBest.text =
                                HtmlCompat.fromHtml(priceString, HtmlCompat.FROM_HTML_MODE_LEGACY)

                            binding.storeButton.setOnClickListener { lifecycleScope.launch { openUrl(url) } }
                        }
                    } else {
                        binding.currentBest.visibility = View.GONE
                        binding.currentBestText.visibility = View.GONE
                    }

                    if (result.data.lowest != null) {
                        result.data.lowest?.apply {
                            binding.historicalLow.text = HtmlCompat.fromHtml(
                                getString(
                                    R.string.price_on_store_with_rebate_template,
                                    price_formatted, store, cut, colors.green, colors.red
                                ), HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                        }
                    } else {
                        binding.historicalLow.visibility = View.GONE
                        binding.historicalLowText.visibility = View.GONE
                    }
                }
                is Result.Error -> {
                    result.errorLogged()
                    parentFragment?.let {
                        if (it is GameFragment) {
                            it.showSnackBarWithRetryAction(R.string.unable_to_load_game) {
                                viewModel.getGameOverview(argPlain)
                            }
                        }
                    }
                }
            }
        })

        binding.addFavoriteButton.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
                param(FirebaseAnalytics.Param.ITEM_ID, argPlain)
            }
            viewModel.addFavorite(argPlain)
        }

        viewModel.favoriteLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success<Favorite> -> {
                    binding.removeFavoriteButton.setOnClickListener {
                        firebaseAnalytics.logEvent(Analytics.Event.REMOVE_FROM_WISHLIST) {
                            param(FirebaseAnalytics.Param.ITEM_ID, argPlain)
                        }
                        viewModel.removeFavorite(result.data.plain)
                    }
                    binding.addFavoriteButton.visibility = View.GONE
                    binding.priceAlertGroup.visibility = View.VISIBLE
                    if (!binding.priceAlertInput.hasFocus()) {
                        result.data.priceThreshold?.let { binding.priceAlertInput.setText(it.toString()) }
                    }
                    binding.priceAlertInput.addTextChangedListener(priceAlertTextWatcher)
                    updatePriceAlertStartIcon()
                }
                is Result.Error -> {
                    result.errorLogged()
                    binding.priceAlertGroup.visibility = View.GONE
                    binding.addFavoriteButton.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Game Overview")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "GameOverviewFragment")
        }
    }

    private fun updatePriceAlertStartIcon() {
        binding.priceAlertInputLayout.startIconDrawable = if (binding.priceAlertInput.text?.isEmpty() == true) {
            getDrawable(resources, R.drawable.ic_alarm_24dp, null)
        } else {
            getDrawable(resources, R.drawable.ic_alarm_on_24dp, null)
        }
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}