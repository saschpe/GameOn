package saschpe.gameon.mobile.game

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.decode.DataSource
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.CustomTabs
import kotlin.math.roundToInt

class GameFragment : Fragment(R.layout.fragment_game) {
    private var paramPlain: String? = null
    private var paramTitle: String? = null
    private val viewModel: GameViewModel by viewModels()
    private lateinit var gameAdapter: GameAdapter
    private lateinit var gameReviewAdapter: GameReviewAdapter
    private lateinit var gamePriceAdapter: GamePriceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            paramPlain = getString(ARG_PLAIN)
            paramTitle = getString(ARG_TITLE)
        }

        gameAdapter = GameAdapter(requireContext())
        gameReviewAdapter = GameReviewAdapter(requireContext())
        gamePriceAdapter = GamePriceAdapter(requireContext())

        requireNotNull(paramPlain)
        paramPlain?.let {
            viewModel.getGameInfo(it)
            viewModel.getGameOverview(it)
            viewModel.getGamePrice(it)
            viewModel.getFavorite(it)
        }
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        // Placeholder
        paramTitle?.let { name.text = it }

        reviewsRecyclerView.apply {
            adapter = gameReviewAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.gameInfoLiveData.observe(this, Observer { gameInfo ->
            // TODO: Only hide once all observers triggered once...
            progressBar.visibility = View.GONE

            paramTitle = gameInfo.title
            name.text = gameInfo.title

            image.load(gameInfo.image) {
                crossfade(true)
                listener(onSuccess = { _: Any, _: DataSource ->
                    val height = (appBarLayout.width * IMAGE_ASPECT_RATIO).roundToInt()
                    val layoutParams = appBarLayout.layoutParams
                    layoutParams.height = height
                    appBarLayout.layoutParams = layoutParams
                })
            }

            val viewModelList = gameInfo.reviews?.map { review ->
                GameReviewAdapter.ViewModel.ReviewViewModel(
                    store = review.key, // TODO: Read from stores instead later
                    review = review.value
                    // TODO: Add onClick handler to open steam page..
                )
            } ?: listOf(
                GameReviewAdapter.ViewModel.CreateReviewViewModel(
                    // TODO: Add onClick handler to add steam review
                )
            )

            lifecycleScope.launch {
                // Wait a little to ensure the recycler is measured and drawn as the job is super-fast
                // Also looks cool...
                delay(200)
                gameReviewAdapter.submitList(viewModelList)
            }

        })

        viewModel.gameOverviewLiveData.observe(this, Observer { gameOverview ->
            val green = ContextCompat.getColor(requireContext(), R.color.green)
            val red = ContextCompat.getColor(requireContext(), R.color.red)

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

            buyButton.setOnClickListener {
                CustomTabs.openUrl(requireContext(), gameOverview.price.url)
            }
        })

        viewModel.gamePriceLiveData.observe(this, Observer { gamePrice ->
            lifecycleScope.launch {
                // Wait a little to ensure the recycler is measured and drawn as the job is super-fast
                // Also looks cool...
                delay(200)
                gamePriceAdapter.submitList(gamePrice.list.map {
                    GamePriceAdapter.ViewModel.PriceViewModel(
                        price = it
                        // TODO: Add onClick handler to open purchase page..
                    )
                })
            }
        })

        viewModel.favoriteLiveData.observe(this, Observer { favorite ->
            if (favorite != null) {
                favoriteButton.icon = requireContext().getDrawable(R.drawable.ic_favorite_24dp)
                favoriteButton.text = requireContext().getString(R.string.remove)
                favoriteButton.setOnClickListener { viewModel.removeFavorite(favorite.plain) }
            } else {
                favoriteButton.icon =
                    requireContext().getDrawable(R.drawable.ic_favorite_border_24dp)
                favoriteButton.text = requireContext().getString(R.string.favorite)
                favoriteButton.setOnClickListener {
                    viewModel.addFavorite(paramPlain!!, paramTitle!!)
                }
            }
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
        const val ARG_TITLE = "title"

        /**
         * Based on what the IsThereAnyDeal API delivers.
         */
        private const val IMAGE_ASPECT_RATIO = 215 / 460.0
    }
}
