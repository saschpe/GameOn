package saschpe.gameon.mobile.game.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_other.*
import kotlinx.coroutines.launch
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.openUrl
import saschpe.gameon.mobile.base.errorLogged
import saschpe.gameon.mobile.game.GameFragment

class GameOtherFragment : Fragment(R.layout.fragment_game_other) {
    private lateinit var reviewsAdapter: GameReviewsAdapter
    private lateinit var argPlain: String
    private val viewModel: GameOtherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argPlain = requireNotNull(arguments?.getString(ARG_PLAIN))
        reviewsAdapter = GameReviewsAdapter(requireContext())
        viewModel.getGameInfo(argPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
        }

        viewModel.gameInfoLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success<GameInfo> -> {
                    val gameInfo = result.data
                    val viewModels = gameInfo.reviews?.map { review ->
                        GameReviewsAdapter.ViewModel.ReviewViewModel(
                            store = review.key,
                            review = review.value,
                            onClick = {
                                // TODO: Add onClick handler to open steam page..
                                // openUrl(requireContext(), gameInfo.urls.game)
                            }
                        )
                    } ?: listOf(GameReviewsAdapter.ViewModel.NoResultsViewModel())

                    reviewsAdapter.submitList(viewModels)

                    isDlcChip.visibility = if (gameInfo.is_dlc) View.VISIBLE else View.GONE
                    achievementsChip.visibility =
                        if (gameInfo.achievements) View.VISIBLE else View.GONE
                    tradingCardsChip.visibility =
                        if (gameInfo.trading_cards) View.VISIBLE else View.GONE
                    earlyAccessChip.visibility =
                        if (gameInfo.early_access) View.VISIBLE else View.GONE
                    isPackageChip.visibility = if (gameInfo.is_package) View.VISIBLE else View.GONE
                    if (!gameInfo.is_dlc && !gameInfo.achievements && !gameInfo.trading_cards && !gameInfo.early_access && !gameInfo.is_package) {
                        perksDivider.visibility = View.GONE
                        perksText.visibility = View.GONE
                    }

                    checkOnProtonDB.setOnClickListener {
                        lifecycleScope.launch { openUrl("$PROTON_DB_SEARCH_URL${gameInfo.title}") }
                    }
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
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Game Other", "GameOtherFragment")
    }

    companion object {
        const val ARG_PLAIN = "plain"
        private const val PROTON_DB_SEARCH_URL = "https://www.protondb.com/search?q="
    }
}