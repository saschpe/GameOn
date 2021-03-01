package saschpe.gameon.mobile.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saschpe.gameon.common.base.app.hideSoftInput
import saschpe.gameon.common.base.app.showSoftInput
import saschpe.gameon.common.base.content.hasScreenWidth
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.SpacingItemDecoration
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Analytics
import saschpe.gameon.mobile.base.NativeAdUnit
import saschpe.gameon.mobile.base.OfferAdapter
import saschpe.gameon.mobile.base.loadAdvertisement
import saschpe.gameon.mobile.databinding.FragmentSearchBinding
import saschpe.gameon.mobile.game.GameFragment

class SearchFragment : Fragment() {
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
    private val noResultsViewModels = listOf(
        OfferAdapter.ViewModel.NoResultsViewModel {
            binding.searchQuery.text?.clear()
            offerAdapter.submitList(listOf())
        }
    )
    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerAdapter = OfferAdapter(requireContext())

        loadAdvertisement(NativeAdUnit.Offers) {
            offerAdapter.submitList(listOf(OfferAdapter.ViewModel.AdvertisementViewModel(it)))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(binding.toolbar, findNavController())

        binding.recyclerView.apply {
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

        binding.searchQuery.doAfterTextChanged { text ->
            if (text?.isNotBlank() == true && text.count() > 2) {
                currentSearchJob?.cancel()
                currentSearchJob = lifecycleScope.launch {
                    delay(100L) // Rate-limit to avoid an update on every key press...
                    binding.progressBar.visibility = View.VISIBLE
                    lastSearch = text.toString()
                    viewModel.search(text.toString())
                }
            }
        }

        viewModel.searchLiveData.observe(viewLifecycleOwner, { result ->
            binding.progressBar.visibility = View.GONE
            val viewModels = when (result) {
                is Result.Success<List<Offer>> -> {
                    when {
                        result.data.isNotEmpty() -> result.data.map { offer ->
                            OfferAdapter.ViewModel.OfferViewModel(lifecycleScope, offer) {
                                findNavController().navigate(
                                    R.id.action_search_to_game,
                                    bundleOf(GameFragment.ARG_PLAIN to offer.plain)
                                )
                            }
                        }
                        else -> noResultsViewModels
                    }
                }
                is Result.Error -> {
                    result.errorLogged()
                    noResultsViewModels
                }
            }

            offerAdapter.submitList(viewModels)
            firebaseAnalytics.logEvent(Analytics.Event.VIEW_SEARCH_RESULTS) {
                lastSearch?.let { term -> param(FirebaseAnalytics.Param.SEARCH_TERM, term) }
                param(FirebaseAnalytics.Param.QUANTITY, viewModels.size.toLong())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Search")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "SearchFragment")
        }
        requireActivity().showSoftInput(binding.searchQuery)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().hideSoftInput()
    }
}