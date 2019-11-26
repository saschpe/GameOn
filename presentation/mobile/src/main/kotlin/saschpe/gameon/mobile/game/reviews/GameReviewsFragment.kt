package saschpe.gameon.mobile.game.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_reviews.*
import saschpe.gameon.mobile.R
import saschpe.log4k.Log

class GameReviewsFragment : Fragment(R.layout.fragment_game_reviews) {
    private lateinit var reviewsAdapter: GameReviewsAdapter
    private lateinit var paramPlain: String
    private val viewModel: GameReviewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = arguments?.getString(ARG_PLAIN)
            ?: throw IllegalArgumentException("Argument 'plain' is null")

        reviewsAdapter = GameReviewsAdapter(requireContext())

        viewModel.getGameInfo(paramPlain)
        Log.debug("XXX")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        viewModel.gameInfoLiveData.observe(this, Observer { gameInfo ->
            // TODO: Only hide once all observers triggered once...
            Log.debug("XXX")

            val viewModelList = gameInfo.reviews?.map { review ->
                GameReviewsAdapter.ViewModel.ReviewViewModel(
                    store = review.key, // TODO: Read from stores instead later
                    review = review.value
                    // TODO: Add onClick handler to open steam page..
                )
            } ?: listOf(
                GameReviewsAdapter.ViewModel.NoResultsViewModel(
                    // TODO: Add onClick handler to add steam review
                )
            )

            Log.debug("XXX")
            reviewsAdapter.submitList(viewModelList)
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}