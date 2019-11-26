package saschpe.gameon.mobile.game.prices

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_prices.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R

class GamePricesFragment : Fragment(R.layout.fragment_game_prices) {
    private lateinit var adapter: GamePricesAdapter
    private lateinit var paramPlain: String
    private val viewModel: GamePricesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = arguments?.getString(ARG_PLAIN)
            ?: throw IllegalArgumentException("Argument 'plain' is null")

        adapter = GamePricesAdapter(requireContext())

        viewModel.getGamePrice(paramPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }

        viewModel.gamePriceLiveData.observe(this, Observer { gamePrice ->
            lifecycleScope.launch {
                // Wait a little to ensure the recycler is measured and drawn as the job is super-fast
                // Also looks cool...
                delay(200)
                adapter.submitList(gamePrice.list.map {
                    GamePricesAdapter.ViewModel.PriceViewModel(
                        price = it
                        // TODO: Add onClick handler to open purchase page..
                    )
                })
            }
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}