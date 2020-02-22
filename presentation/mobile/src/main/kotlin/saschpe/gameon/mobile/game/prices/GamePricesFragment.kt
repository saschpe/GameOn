package saschpe.gameon.mobile.game.prices

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_game_prices.*
import saschpe.gameon.common.content.hasScreenWidth
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Analytics
import saschpe.gameon.mobile.base.customtabs.CustomTabs.openUrl

class GamePricesFragment : Fragment(R.layout.fragment_game_prices) {
    private lateinit var pricesAdapter: GamePricesAdapter
    private lateinit var argPlain: String
    private val viewModel: GamePricesViewModel by viewModels()
    private val gridLayoutSpanCount
        get() = when {
            requireContext().hasScreenWidth(600) -> 2
            else -> 1
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argPlain = requireNotNull(arguments?.getString(ARG_PLAIN))

        pricesAdapter = GamePricesAdapter(requireContext())

        viewModel.getGamePrice(argPlain)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            adapter = pricesAdapter
            layoutManager = GridLayoutManager(context, gridLayoutSpanCount)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.gamePriceLiveData.observe(viewLifecycleOwner, Observer { gamePrice ->
            pricesAdapter.submitList(gamePrice.list.map {
                GamePricesAdapter.ViewModel.PriceViewModel(
                    price = it,
                    onClick = {
                        firebaseAnalytics.logEvent(
                            Analytics.Event.VISIS_EXTERNAL_SHOP, bundleOf(
                                Analytics.Param.SHOP_NAME to it.shop, Analytics.Param.SHOP_ITEM_URL to it.url
                            )
                        )
                        openUrl(requireContext(), it.url)
                    }
                )
            })
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Game Prices", null)
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}