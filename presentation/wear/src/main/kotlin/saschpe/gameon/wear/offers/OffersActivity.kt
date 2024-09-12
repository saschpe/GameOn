package saschpe.gameon.wear.offers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.WearableLinearLayoutManager
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.SpacingItemDecoration
import saschpe.gameon.common.offers.OffersViewModel
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.wear.base.*
import saschpe.gameon.wear.databinding.ActivityOffersBinding
import saschpe.gameon.common.R as CommonR

class OffersActivity :
    AppCompatActivity(),
    AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var ambientController: AmbientModeSupport.AmbientController
    private lateinit var binding: ActivityOffersBinding
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var viewModel: OffersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOffersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ambientController = AmbientModeSupport.attach(this)
        offerAdapter = OfferAdapter(this)

        binding.topNavigationDrawer.setAdapter(TopNavigationDrawerAdapter(this))
        binding.topNavigationDrawer.setCurrentItem(TOP_NAVIGATION_SEARCH_POSITION, false)
        binding.topNavigationDrawer.addOnItemSelectedListener(TopNavigationItemSelectedListener(this))
        binding.topNavigationDrawer.controller.peekDrawer()
        binding.recyclerView.apply {
            adapter = offerAdapter
            layoutManager = WearableLinearLayoutManager(this@OffersActivity)
            addItemDecoration(SpacingItemDecoration(context, CommonR.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel = ViewModelProvider(this)[OffersViewModel::class.java]
        viewModel.dealLiveData.observe(this) { result ->
            val viewModels = when (result) {
                is Result.Success<List<Offer>> -> result.data.map { offer ->
                    OfferAdapter.ViewModel.OfferViewModel(lifecycleScope, offer) {
                        /*findNavController().navigate(
                            R.id.action_offers_to_game,
                            bundleOf(GameFragment.ARG_PLAIN to offer.plain)
                        )*/
                    }
                }

                is Result.Error -> {
                    result.errorLogged()
                    listOf(
                        OfferAdapter.ViewModel.NoResultsViewModel {
                            offerAdapter.submitList(listOf())
                        }
                    )
                }
            }
            offerAdapter.submitList(viewModels)
        }

        viewModel.getDeals()
    }

    override fun getAmbientCallback() = ClockAmbientCallback(binding.drawerLayout, binding.topNavigationDrawer, theme, binding.clock)
}
