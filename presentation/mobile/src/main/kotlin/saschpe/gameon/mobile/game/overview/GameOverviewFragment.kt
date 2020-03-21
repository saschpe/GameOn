package saschpe.gameon.mobile.game.overview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.api.load
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_game_overview.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.Module.colors
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Analytics
import saschpe.gameon.mobile.base.customtabs.openUrl

class GameOverviewFragment : Fragment(R.layout.fragment_game_overview) {
    private lateinit var argPlain: String
    private val viewModel: GameOverviewViewModel by viewModels()
    private var priceAlertTextWatcher = object : TextWatcher {
        private var currentJob: Job? = null

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        override fun afterTextChanged(text: Editable?) {
            updatePriceAlertStartIcon()

            currentJob?.cancel() // Only have one, i.e. the latest, running...
            currentJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(200L) // Rate-limit to avoid an update on every key press...

                viewModel.favoriteLiveData.value?.let {
                    var priceAlertString = text?.toString()
                    if (priceAlertString?.isBlank() == true) {
                        priceAlertString = null
                    }
                    firebaseAnalytics.logEvent(
                        Analytics.Event.UPDATE_ON_WISHLIST, bundleOf(
                            FirebaseAnalytics.Param.ITEM_ID to argPlain,
                            FirebaseAnalytics.Param.PRICE to priceAlertString
                        )
                    )
                    viewModel.updateFavorite(it.copy(priceThreshold = priceAlertString?.toDouble()))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argPlain = requireNotNull(arguments?.getString(ARG_PLAIN))

        viewModel.getGameInfo(argPlain)
        viewModel.getGameOverview(argPlain)
        viewModel.getFavorite(argPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.gameInfoLiveData.observe(viewLifecycleOwner, Observer { gameInfo ->
            cover.load(gameInfo.image) {
                placeholder(R.drawable.placeholder)
                crossfade(true)
            }
        })

        viewModel.gameOverviewLiveData.observe(viewLifecycleOwner, Observer { gameOverview ->
            if (gameOverview.price != null) {
                gameOverview.price?.run {
                    val priceString = if (cut == 0) {
                        getString(
                            R.string.price_on_store_colored_template,
                            price_formatted,
                            store,
                            colors.green
                        )
                    } else {
                        getString(
                            R.string.price_on_store_with_rebate_template,
                            price_formatted, store, cut, colors.green, colors.red
                        )
                    }
                    currentBest.text =
                        HtmlCompat.fromHtml(priceString, HtmlCompat.FROM_HTML_MODE_LEGACY)

                    storeButton.setOnClickListener { openUrl(url) }
                }
            } else {
                currentBest.visibility = View.GONE
                currentBestText.visibility = View.GONE
            }

            if (gameOverview.lowest != null) {
                gameOverview.lowest?.apply {
                    historicalLow.text = HtmlCompat.fromHtml(
                        getString(
                            R.string.price_on_store_with_rebate_template,
                            price_formatted, store, cut, colors.green, colors.red
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
            } else {
                historicalLow.visibility = View.GONE
                historicalLowText.visibility = View.GONE
            }
        })

        addFavoriteButton.setOnClickListener {
            firebaseAnalytics.logEvent(
                FirebaseAnalytics.Event.ADD_TO_WISHLIST,
                bundleOf(FirebaseAnalytics.Param.ITEM_ID to argPlain)
            )
            viewModel.addFavorite(argPlain)
        }

        viewModel.favoriteLiveData.observe(viewLifecycleOwner, Observer { favorite ->
            requireContext().run {
                if (favorite != null) {
                    removeFavoriteButton.setOnClickListener {
                        firebaseAnalytics.logEvent(
                            Analytics.Event.REMOVE_FROM_WISHLIST,
                            bundleOf(FirebaseAnalytics.Param.ITEM_ID to argPlain)
                        )
                        viewModel.removeFavorite(favorite.plain)
                    }

                    addFavoriteButton.visibility = View.GONE
                    priceAlertGroup.visibility = View.VISIBLE
                    if (!priceAlertInput.hasFocus()) {
                        favorite.priceThreshold?.let { priceAlertInput.setText(it.toString()) }
                    }
                    priceAlertInput.addTextChangedListener(priceAlertTextWatcher)
                    updatePriceAlertStartIcon()
                } else {
                    priceAlertGroup.visibility = View.GONE
                    addFavoriteButton.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(
            requireActivity(), "Game Overview", "GameOverviewFragment"
        )
    }

    private fun updatePriceAlertStartIcon() {
        priceAlertInputLayout.startIconDrawable = if (priceAlertInput.text?.isEmpty() == true) {
            getDrawable(resources, R.drawable.ic_alarm_24dp, null)
        } else {
            getDrawable(resources, R.drawable.ic_alarm_on_24dp, null)
        }
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}