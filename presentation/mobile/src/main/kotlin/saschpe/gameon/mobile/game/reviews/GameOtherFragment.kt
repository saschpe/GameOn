package saschpe.gameon.mobile.game.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_misc.*
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R

class GameOtherFragment : Fragment(R.layout.fragment_game_misc) {
    private lateinit var reviewsAdapter: GameReviewsAdapter
    private lateinit var paramPlain: String
    private val viewModel: GameOtherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = requireNotNull(arguments?.getString(ARG_PLAIN))

        reviewsAdapter = GameReviewsAdapter(requireContext())

        viewModel.getGameInfo(paramPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
        }

        viewModel.gameInfoLiveData.observe(viewLifecycleOwner, Observer { gameInfo ->
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
            achievementsChip.visibility = if (gameInfo.achievements) View.VISIBLE else View.GONE
            tradingCardsChip.visibility = if (gameInfo.trading_cards) View.VISIBLE else View.GONE
            earlyAccessChip.visibility = if (gameInfo.early_access) View.VISIBLE else View.GONE
            isPackageChip.visibility = if (gameInfo.is_package) View.VISIBLE else View.GONE
            if (!gameInfo.is_dlc && !gameInfo.achievements && !gameInfo.trading_cards && !gameInfo.early_access && !gameInfo.is_package) {
                perksDivider.visibility = View.GONE
                perksText.visibility = View.GONE
            }
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}