package saschpe.gameon.mobile.game.overview

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_game_overview.*
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.CustomTabs.openUrl

class GameOverviewFragment : Fragment(R.layout.fragment_game_overview) {
    private lateinit var paramPlain: String
    private val viewModel: GameOverviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = requireNotNull(arguments?.getString(ARG_PLAIN))

        viewModel.getGameInfo(paramPlain)
        viewModel.getGameOverview(paramPlain)
        viewModel.getFavorite(paramPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gameInfoLiveData.observe(this, Observer { gameInfo ->
            name.text = gameInfo.title

            isDlcChip.visibility = if (gameInfo.is_dlc) View.VISIBLE else View.GONE
            achievementsChip.visibility = if (gameInfo.achievements) View.VISIBLE else View.GONE
            tradingCardsChip.visibility = if (gameInfo.trading_cards) View.VISIBLE else View.GONE
            earlyAccessChip.visibility = if (gameInfo.early_access) View.VISIBLE else View.GONE
            isPackageChip.visibility = if (gameInfo.is_package) View.VISIBLE else View.GONE
            if (!gameInfo.is_dlc && !gameInfo.achievements && !gameInfo.trading_cards && !gameInfo.early_access && !gameInfo.is_package) {
                divider3.visibility = View.GONE
            }
        })

        viewModel.gameOverviewLiveData.observe(this, Observer { gameOverview ->
            val green = ContextCompat.getColor(requireContext(), R.color.green)
            val red = ContextCompat.getColor(requireContext(), R.color.red)

            if (gameOverview.price != null) {
                gameOverview.price?.run {
                    val priceString = if (cut == 0) {
                        getString(R.string.price_on_store_template, price, store, green)
                    } else {
                        getString(
                            R.string.price_on_store_with_rebate_template,
                            price, store, cut, green, red
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
                            price, store, cut, green, red
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
            } else {
                historicalLow.visibility = View.GONE
                historicalLowText.visibility = View.GONE
            }

            if (gameOverview.price == null && gameOverview.lowest == null) {
                divider.visibility = View.GONE
            }
        })

        viewModel.favoriteLiveData.observe(this, Observer { favorite ->
            requireContext().run {
                if (favorite != null) {
                    favoriteButton.icon = getDrawable(R.drawable.ic_favorite_24dp)
                    favoriteButton.text = getString(R.string.remove_from_favorites)
                    favoriteButton.setOnClickListener { viewModel.removeFavorite(favorite.plain) }
                } else {
                    favoriteButton.icon = getDrawable(R.drawable.ic_favorite_border_24dp)
                    favoriteButton.text = getString(R.string.add_to_favorites)
                    favoriteButton.setOnClickListener { viewModel.addFavorite(paramPlain) }
                }
            }
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}