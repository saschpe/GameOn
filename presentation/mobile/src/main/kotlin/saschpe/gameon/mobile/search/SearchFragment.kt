package saschpe.gameon.mobile.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.app.hideSoftInput
import saschpe.gameon.common.app.showSoftInput
import saschpe.gameon.common.content.hasScreenWidth
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Analytics
import saschpe.gameon.mobile.base.NativeAdUnit
import saschpe.gameon.mobile.base.OfferAdapter
import saschpe.gameon.mobile.base.loadAdvertisement
import saschpe.gameon.mobile.game.GameFragment

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var currentSearchJob: Job? = null
    private val gridLayoutSpanCount
        get() = when {
            requireContext().hasScreenWidth(720) -> 4
            requireContext().hasScreenWidth(600) -> 3
            requireContext().hasScreenWidth(360) -> 2
            else -> 1
        }
    private var lastSearch: String? = null
    private lateinit var offerAdapter: OfferAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerAdapter = OfferAdapter(requireContext())

        loadAdvertisement(NativeAdUnit.Offers) {
            offerAdapter.submitList(listOf(OfferAdapter.ViewModel.AdvertisementViewModel(it)))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(toolbar, findNavController())

        recyclerView.apply {
            adapter = offerAdapter
            layoutManager = GridLayoutManager(context, gridLayoutSpanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        when (offerAdapter.getItemViewType(position)) {
                            OfferAdapter.VIEW_TYPE_ADVERTISEMENT -> gridLayoutSpanCount
                            OfferAdapter.VIEW_TYPE_NO_RESULTS -> gridLayoutSpanCount
                            else -> 1
                        }
                }
            }
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        searchQuery.doAfterTextChanged { text ->
            if (text?.isNotBlank() == true && text.count() > 2) {
                currentSearchJob?.cancel()
                currentSearchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(100L) // Rate-limit to avoid an update on every key press...
                    progressBar.visibility = View.VISIBLE
                    lastSearch = text.toString()
                    viewModel.search(text.toString())
                }
            }
        }

        viewModel.searchLiveData.observe(viewLifecycleOwner, Observer { offers ->
            firebaseAnalytics.logEvent(Analytics.Event.VIEW_SEARCH_RESULTS) {
                lastSearch?.let { term -> param(FirebaseAnalytics.Param.SEARCH_TERM, term) }
            }
            val viewModels = when {
                offers.isNotEmpty() -> offers.map { offer ->
                    OfferAdapter.ViewModel.OfferViewModel(lifecycleScope, offer) { transitionView ->
                        val transitionName = offer.plain
                        transitionView.transitionName = transitionName
                        findNavController().navigate(
                            R.id.action_search_to_game,
                            bundleOf(GameFragment.ARG_PLAIN to offer.plain),
                            null,
                            FragmentNavigatorExtras(transitionView to transitionName)
                        )
                    }
                }
                else -> listOf(
                    OfferAdapter.ViewModel.NoResultsViewModel {
                        searchQuery.text?.clear()
                        offerAdapter.submitList(listOf())
                    }
                )
            }
            offerAdapter.submitList(viewModels)
            progressBar.visibility = View.GONE
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Search", "SearchFragment")
        requireActivity().showSoftInput(searchQuery)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().hideSoftInput()
    }
}