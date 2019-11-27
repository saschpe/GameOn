package saschpe.gameon.mobile.game.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_reviews.*
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R

class GameReviewsFragment : Fragment(R.layout.fragment_game_reviews) {
    private lateinit var reviewsAdapter: GameReviewsAdapter
    private lateinit var paramPlain: String
    private val viewModel: GameReviewsViewModel by viewModels()

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
            setHasFixedSize(true)
        }

        viewModel.gameInfoLiveData.observe(this, Observer { gameInfo ->
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
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}