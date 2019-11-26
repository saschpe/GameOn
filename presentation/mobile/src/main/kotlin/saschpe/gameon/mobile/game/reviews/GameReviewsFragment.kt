package saschpe.gameon.mobile.game.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_reviews.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R

class GameReviewsFragment : Fragment(R.layout.fragment_game_reviews) {
    private lateinit var adapter: GameReviewsAdapter
    private lateinit var paramPlain: String
    private val viewModel: GameReviewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = arguments?.getString(ARG_PLAIN)
            ?: throw IllegalArgumentException("Argument 'plain' is null")

        adapter = GameReviewsAdapter(requireContext())

        viewModel.getGameInfo(paramPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }

        viewModel.gameInfoLiveData.observe(this, Observer { gameInfo ->
            // TODO: Only hide once all observers triggered once...

            val viewModelList = gameInfo.reviews?.map { review ->
                GameReviewsAdapter.ViewModel.ReviewViewModel(
                    store = review.key, // TODO: Read from stores instead later
                    review = review.value
                    // TODO: Add onClick handler to open steam page..
                )
            } ?: listOf(
                GameReviewsAdapter.ViewModel.CreateReviewViewModel(
                    // TODO: Add onClick handler to add steam review
                )
            )

            lifecycleScope.launch {
                // Wait a little to ensure the recycler is measured and drawn as the job is super-fast
                // Also looks cool...
                delay(200)
                adapter.submitList(viewModelList)
            }
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}