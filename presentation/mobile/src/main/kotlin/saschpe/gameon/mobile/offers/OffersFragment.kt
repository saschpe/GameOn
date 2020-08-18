package saschpe.gameon.mobile.offers

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.android.synthetic.main.fragment_offers.*
import saschpe.gameon.common.base.content.hasScreenWidth
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.SpacingItemDecoration
import saschpe.gameon.common.offers.OffersViewModel
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.NativeAdUnit
import saschpe.gameon.mobile.base.OfferAdapter
import saschpe.gameon.mobile.base.loadAdvertisement
import saschpe.gameon.mobile.game.GameFragment

class OffersFragment : Fragment(R.layout.fragment_offers) {
    private val gridLayoutSpanCount
        get() = when {
            requireContext().hasScreenWidth(720) -> 4
            requireContext().hasScreenWidth(600) -> 3
            requireContext().hasScreenWidth(360) -> 2
            else -> 1
        }
    private lateinit var offerAdapter: OfferAdapter
    private val viewModel: OffersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerAdapter = OfferAdapter(requireContext())

        viewModel.getDeals()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(toolbar, findNavController())
        toolbar.inflateMenu(R.menu.menu_offers)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> findNavController().navigate(R.id.action_offers_to_search)
                R.id.help -> findNavController().navigate(R.id.action_offers_to_help)
                R.id.settings -> findNavController().navigate(R.id.action_offers_to_settings)
            }
            true
        }

        loadAdvertisement(NativeAdUnit.Search) {
            if (progressBar?.visibility == View.VISIBLE) {
                // Only submit ad if we're still loading offers
                offerAdapter.submitList(listOf(OfferAdapter.ViewModel.AdvertisementViewModel(it)))
            }
        }

        recyclerView.apply {
            adapter = offerAdapter
            layoutManager = GridLayoutManager(context, gridLayoutSpanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        when (offerAdapter.getItemViewType(position)) {
                            OfferAdapter.VIEW_TYPE_ADVERTISEMENT -> gridLayoutSpanCount
                            else -> 1
                        }
                }
            }
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.dealLiveData.observe(viewLifecycleOwner, Observer { result ->
            progressBar.visibility = View.GONE
            val viewModels = when (result) {
                is Result.Success<List<Offer>> -> result.data.map { offer ->
                    OfferAdapter.ViewModel.OfferViewModel(lifecycleScope, offer) {
                        findNavController().navigate(
                            R.id.action_offers_to_game,
                            bundleOf(GameFragment.ARG_PLAIN to offer.plain)
                        )
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
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Offers")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "OffersFragment")
        }
    }
}