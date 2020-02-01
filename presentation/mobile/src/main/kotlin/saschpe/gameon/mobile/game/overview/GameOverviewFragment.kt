package saschpe.gameon.mobile.game.overview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.api.load
import kotlinx.android.synthetic.main.fragment_game_overview.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.Module.colors
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.CustomTabs.openUrl

class GameOverviewFragment : Fragment(R.layout.fragment_game_overview) {
    private lateinit var paramPlain: String
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
                    viewModel.updateFavorite(it.copy(priceThreshold = priceAlertString?.toDouble()))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = requireNotNull(arguments?.getString(ARG_PLAIN))

        viewModel.getGameInfo(paramPlain)
        viewModel.getGameOverview(paramPlain)
        viewModel.getFavorite(paramPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.gameInfoLiveData.observe(viewLifecycleOwner, Observer { gameInfo ->
            Coil.loader().load(requireContext(), gameInfo.image) {
                crossfade(true)
                target(onSuccess = { cover.setImageDrawable(it) })
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

                    storeButton.setOnClickListener { openUrl(requireContext(), url) }
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

        viewModel.favoriteLiveData.observe(viewLifecycleOwner, Observer { favorite ->
            requireContext().run {
                if (favorite != null) {
                    favoriteButton.icon = getDrawable(R.drawable.ic_favorite_24dp)
                    favoriteButton.text = getString(R.string.remove_from_favorites)
                    favoriteButton.setOnClickListener { viewModel.removeFavorite(favorite.plain) }

                    priceAlertGroup.visibility = View.VISIBLE
                    if (!priceAlertInput.hasFocus()) {
                        favorite.priceThreshold?.let { priceAlertInput.setText(it.toString()) }
                    }
                    priceAlertInput.addTextChangedListener(priceAlertTextWatcher)
                    updatePriceAlertStartIcon()
                } else {
                    favoriteButton.icon = getDrawable(R.drawable.ic_favorite_border_24dp)
                    favoriteButton.text = getString(R.string.add_to_favorites)
                    favoriteButton.setOnClickListener { viewModel.addFavorite(paramPlain) }

                    priceAlertGroup.visibility = View.GONE
                }
            }
        })
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