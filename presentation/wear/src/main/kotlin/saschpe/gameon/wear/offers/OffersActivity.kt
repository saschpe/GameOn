package saschpe.gameon.wear.offers

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.wear.widget.WearableLinearLayoutManager
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
import saschpe.gameon.wear.databinding.ActivityOffersBinding

class OffersActivity : BaseActivity() {
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var viewModel: OffersViewModel
    private lateinit var binding: ActivityOffersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOffersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopNavigation(binding.topNavigation, TOP_NAVIGATION_SEARCH_POSITION)

        offerAdapter = OfferAdapter(this)
        binding.recyclerView.apply {
            adapter = offerAdapter
            layoutManager = WearableLinearLayoutManager(this@OffersActivity)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel = ViewModelProvider(this).get(OffersViewModel::class.java)
        viewModel.dealLiveData.observe(this, { result ->
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

    override fun getAmbientCallback() =
        ClockAmbientCallback(binding.drawerLayout, binding.topNavigation, theme, binding.clock)
}
