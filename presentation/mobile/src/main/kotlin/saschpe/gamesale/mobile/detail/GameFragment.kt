package saschpe.gamesale.mobile.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import coil.api.load
import coil.decode.DataSource
import kotlinx.android.synthetic.main.fragment_game.*
import saschpe.gamesale.mobile.R
import kotlin.math.roundToInt

class GameFragment : Fragment(R.layout.fragment_game) {
    private var paramPlain: String? = null
    private var paramTitle: String? = null
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            paramPlain = getString(ARG_PLAIN)
            paramTitle = getString(ARG_TITLE)
        }

        requireNotNull(paramPlain)
        paramPlain?.let {
            viewModel.getGameInfo(it)
            viewModel.getGameOverview(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        // Placeholder
        paramTitle?.let { name.text = it }

        viewModel.gameInfoLiveData.observe(this, Observer { gameInfo ->
            progressBar.visibility = View.GONE
            name.text = gameInfo.title
            image.load(gameInfo.image) {
                crossfade(true)
                listener(onSuccess = { _: Any, _: DataSource ->
                    val height = (appBarLayout.width * IATD_IMAGE_ASPECT_RATIO).roundToInt()
                    val layoutParams = appBarLayout.layoutParams
                    layoutParams.height = height
                    appBarLayout.layoutParams = layoutParams
                })
                placeholder(R.mipmap.ic_launcher)
            }
        })

        viewModel.gameOverviewLiveData.observe(this, Observer { gameOverview ->
            val green = ContextCompat.getColor(requireContext(), R.color.deal_green)
            val red = ContextCompat.getColor(requireContext(), R.color.deal_red)



            currentBest.text = HtmlCompat.fromHtml(
                getString(
                    R.string.pricing_template,
                    gameOverview.price.price,
                    gameOverview.price.store,
                    gameOverview.price.cut,
                    green, red
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            historicalLow.text = HtmlCompat.fromHtml(
                getString(
                    R.string.pricing_template,
                    gameOverview.lowest.price,
                    gameOverview.lowest.store,
                    gameOverview.lowest.cut,
                    green, red
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
        const val ARG_TITLE = "title"
        const val IATD_IMAGE_ASPECT_RATIO = 215 / 460.0
    }
}
