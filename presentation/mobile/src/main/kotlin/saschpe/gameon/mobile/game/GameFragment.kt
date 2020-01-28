package saschpe.gameon.mobile.game

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import coil.Coil
import coil.api.load
import kotlinx.android.synthetic.main.fragment_game.*
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.overview.GameOverviewFragment
import saschpe.gameon.mobile.game.prices.GamePricesFragment
import saschpe.gameon.mobile.game.reviews.GameReviewsFragment
import kotlin.math.roundToInt

class GameFragment : Fragment(R.layout.fragment_game) {
    private lateinit var paramPlain: String
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = requireNotNull(arguments?.getString(ARG_PLAIN))
        viewModel.getGameInfo(paramPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(toolbar, findNavController())

        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter =
            GameFragmentPagerAdapter(requireContext(), paramPlain, childFragmentManager)

        viewModel.gameInfoLiveData.observe(viewLifecycleOwner, Observer { gameInfo ->
            progressBar.visibility = View.GONE
            Coil.loader().load(requireContext(), gameInfo.image) {
                crossfade(true)
                target(onSuccess = {
                    toolbar.background = it
                    val height = (toolbar.width * IMAGE_ASPECT_RATIO).roundToInt()
                    val layoutParams = toolbar.layoutParams
                    layoutParams.height = height
                    toolbar.layoutParams = layoutParams
                })
            }
        })
    }

    private class GameFragmentPagerAdapter(
        val context: Context, val plain: String, fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount() = 3

        override fun getItem(position: Int) = when (position) {
            0 -> GameOverviewFragment().apply {
                arguments = bundleOf(GameOverviewFragment.ARG_PLAIN to plain)
            }
            1 -> GamePricesFragment().apply {
                arguments = bundleOf(GamePricesFragment.ARG_PLAIN to plain)
            }
            else -> GameReviewsFragment().apply {
                arguments = bundleOf(GameReviewsFragment.ARG_PLAIN to plain)
            }
        }

        override fun getPageTitle(position: Int) = when (position) {
            0 -> context.getString(R.string.overview)
            1 -> context.getString(R.string.prices)
            else -> context.getString(R.string.reviews)
        }
    }

    companion object {
        const val ARG_PLAIN = "plain"

        /**
         * Based on what the IsThereAnyDeal API delivers.
         */
        private const val IMAGE_ASPECT_RATIO = 215 / 460.0
    }
}
