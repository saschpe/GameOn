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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.android.synthetic.main.fragment_game.*
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.overview.GameOverviewFragment
import saschpe.gameon.mobile.game.prices.GamePricesFragment
import saschpe.gameon.mobile.game.reviews.GameOtherFragment

class GameFragment : Fragment(R.layout.fragment_game) {
    private lateinit var argPlain: String
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argPlain = requireNotNull(arguments?.getString(ARG_PLAIN))
        viewModel.getGameInfo(argPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(toolbar, findNavController())

        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter =
            GameFragmentPagerAdapter(requireContext(), argPlain, childFragmentManager)

        viewModel.gameInfoLiveData.observe(viewLifecycleOwner, Observer { gameInfo ->
            toolbar.title = gameInfo.title

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
                param(FirebaseAnalytics.Param.ITEM_ID, argPlain)
                param(FirebaseAnalytics.Param.ITEM_NAME, gameInfo.title)
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
            else -> GameOtherFragment().apply {
                arguments = bundleOf(GameOtherFragment.ARG_PLAIN to plain)
            }
        }

        override fun getPageTitle(position: Int) = when (position) {
            0 -> context.getString(R.string.overview)
            1 -> context.getString(R.string.prices)
            else -> context.getString(R.string.other)
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Game", "GameFragment")
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}
