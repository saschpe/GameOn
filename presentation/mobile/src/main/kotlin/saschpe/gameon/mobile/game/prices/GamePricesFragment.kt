package saschpe.gameon.mobile.game.prices

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_prices.*
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.CustomTabs.openUrl

class GamePricesFragment : Fragment(R.layout.fragment_game_prices) {
    private lateinit var pricesAdapter: GamePricesAdapter
    private lateinit var paramPlain: String
    private val viewModel: GamePricesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramPlain = requireNotNull(arguments?.getString(ARG_PLAIN))

        pricesAdapter = GamePricesAdapter(requireContext())

        viewModel.getGamePrice(paramPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = pricesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.gamePriceLiveData.observe(this, Observer { gamePrice ->
            pricesAdapter.submitList(gamePrice.list.map {
                GamePricesAdapter.ViewModel.PriceViewModel(
                    price = it,
                    onClick = { openUrl(requireContext(), it.url) }
                )
            })
        })
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}