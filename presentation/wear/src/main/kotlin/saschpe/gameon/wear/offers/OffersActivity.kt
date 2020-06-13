package saschpe.gameon.wear.offers

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_offers.*
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.offers.OffersViewModel
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.wear.R
import saschpe.gameon.wear.base.BaseActivity
import saschpe.gameon.wear.base.ClockAmbientCallback
import saschpe.gameon.wear.base.TOP_NAVIGATION_SEARCH_POSITION

class OffersActivity : BaseActivity(R.layout.activity_offers, TOP_NAVIGATION_SEARCH_POSITION) {
    private lateinit var viewModel: OffersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OffersViewModel::class.java)
        viewModel.getDeals()
        viewModel.dealLiveData.observe(this, Observer { result ->
            val viewModels = when (result) {
                is Result.Success<List<Offer>> -> result.data.map { offer ->
                    /* OfferAdapter.ViewModel.OfferViewModel(lifecycleScope, offer) {
                         findNavController().navigate(
                             R.id.action_offers_to_game,
                             bundleOf(GameFragment.ARG_PLAIN to offer.plain)
                         )
                     }*/
                }
                is Result.Error -> {
                    result.errorLogged()
                    /* result.errorLogged()
                     listOf(OfferAdapter.ViewModel.NoResultsViewModel {
                         offerAdapter.submitList(listOf())
                     })*/
                }
            }
            // TODO: Adapter:
        })
    }

    override fun getAmbientCallback() = ClockAmbientCallback(drawerLayout, topNavigationDrawer, theme, clock)
}
