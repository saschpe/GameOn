package saschpe.gameon.mobile.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.databinding.FragmentGameBinding
import saschpe.gameon.mobile.game.overview.GameOverviewFragment
import saschpe.gameon.mobile.game.prices.GamePricesFragment
import saschpe.gameon.mobile.game.reviews.GameOtherFragment
import saschpe.gameon.common.R as CommonR

class GameFragment : Fragment(R.layout.fragment_game) {
    private lateinit var argPlain: String
    private val viewModel: GameViewModel by viewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argPlain = requireNotNull(arguments?.getString(ARG_PLAIN))
        viewModel.getGameInfo(argPlain)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(binding.toolbar, findNavController())

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = GameFragmentPagerAdapter(requireContext(), argPlain, childFragmentManager)

        viewModel.gameInfoLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success<GameInfo> -> {
                    binding.toolbar.title = result.data.title
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
                        param(FirebaseAnalytics.Param.ITEM_ID, argPlain)
                        param(FirebaseAnalytics.Param.ITEM_NAME, result.data.title)
                    }
                }

                is Result.Error -> {
                    result.errorLogged()
                    showSnackBarWithRetryAction(CommonR.string.unable_to_load_game) {
                        viewModel.getGameInfo(argPlain)
                    }
                }
            }
        }
    }

    private class GameFragmentPagerAdapter(
        val context: Context,
        val plain: String,
        fragmentManager: FragmentManager,
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
            0 -> context.getString(CommonR.string.overview)
            1 -> context.getString(CommonR.string.prices)
            else -> context.getString(CommonR.string.other)
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Game")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "GameFragment")
        }
    }

    fun showSnackBarWithRetryAction(
        @StringRes resId: Int,
        retryCallback: () -> Unit,
    ) = Snackbar
        .make(binding.coordinatorLayout, getString(resId), Snackbar.LENGTH_INDEFINITE)
        .setAction(CommonR.string.retry) { retryCallback.invoke() }
        .show()

    companion object {
        const val ARG_PLAIN = "plain"
    }
}
