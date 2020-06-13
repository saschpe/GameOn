package saschpe.gameon.wear.offers

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.wear.widget.WearableLinearLayoutManager
import kotlinx.android.synthetic.main.activity_offers.*
import kotlinx.coroutines.GlobalScope
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.SpacingItemDecoration
import saschpe.gameon.common.offers.OffersViewModel
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.ClockAmbientCallback
import saschpe.gameon.wear.base.OfferAdapter
import saschpe.gameon.wear.base.TOP_NAVIGATION_SEARCH_POSITION

class OffersActivity : BaseActivity(R.layout.activity_offers, TOP_NAVIGATION_SEARCH_POSITION) {
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var viewModel: OffersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerAdapter = OfferAdapter(this)

        recyclerView.apply {
            adapter = offerAdapter
            layoutManager = WearableLinearLayoutManager(this@OffersActivity)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel = ViewModelProvider(this).get(OffersViewModel::class.java)
        viewModel.dealLiveData.observe(this, Observer { result ->
            val viewModels = when (result) {
                is Result.Success<List<Offer>> -> result.data.map { offer ->
                     OfferAdapter.ViewModel.OfferViewModel(GlobalScope, offer) {
                         /*findNavController().navigate(
                             R.id.action_offers_to_game,
                             bundleOf(GameFragment.ARG_PLAIN to offer.plain)
                         )*/
                     }
                }
                is Result.Error -> {
                    result.errorLogged()
                    listOf(OfferAdapter.ViewModel.NoResultsViewModel {
                        offerAdapter.submitList(listOf())
                    })
                }
            }
            offerAdapter.submitList(viewModels)
        })

        viewModel.getDeals()
    }

    override fun getAmbientCallback() = ClockAmbientCallback(drawerLayout, topNavigationDrawer, theme, clock)
}
