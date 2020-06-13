package saschpe.gameon.mobile.game.prices

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.android.synthetic.main.fragment_game_prices.*
import kotlinx.coroutines.launch
import saschpe.gameon.common.base.content.hasScreenWidth
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.SpacingItemDecoration
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GamePrice
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Analytics
import saschpe.gameon.mobile.base.customtabs.openUrl
import saschpe.gameon.mobile.game.GameFragment

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

        viewModel.gamePriceLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success<GamePrice> -> pricesAdapter.submitList(result.data.list.map {
                    GamePricesAdapter.ViewModel.PriceViewModel(it) {
                        firebaseAnalytics.logEvent(Analytics.Event.VISIT_EXTERNAL_SHOP) {
                            param(Analytics.Param.SHOP_NAME, it.shop.name)
                            param(Analytics.Param.SHOP_ITEM_URL, it.url)
                        }
                        lifecycleScope.launch { openUrl(it.url) }
                    }
                })
                is Result.Error -> {
                    result.errorLogged()
                    parentFragment?.let {
                        if (it is GameFragment) {
                            it.showSnackBarWithRetryAction(R.string.unable_to_load_game_prices) {
                                viewModel.getGamePrice(argPlain)
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Game Prices", "GamePricesFragment")
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}