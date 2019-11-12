package saschpe.gamesale.mobile.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gamesale.common.recyclerview.SpacingItemDecoration
import saschpe.gamesale.mobile.R

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var offerAdapter: OfferAdapter

    @SuppressLint("HandlerLeak")
    private val delayedSearchHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == MESSAGE_UPDATE_SEARCH) {
                updateSearch()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerAdapter = OfferAdapter(requireContext())
        viewModel.getDeals()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dealLiveData.observe(this, Observer { offers ->
            progressBar.visibility = View.GONE
            offerAdapter.submitList(offers.map { offer ->
                OfferAdapter.ViewModel.OfferViewModel(
                    offer = offer,
                    onClick = {
                        // TODO: Enter detail view and fire detail query
                    }
                )
            })
        })

        recyclerView.apply {
            adapter = offerAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        searchQuery.doAfterTextChanged { text ->
            if (text?.count() ?: 0 > 2) {
                delayedSearchHandler.removeMessages(MESSAGE_UPDATE_SEARCH)
                delayedSearchHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_SEARCH, 300)
            }
        }
    }

    private fun updateSearch() {
        progressBar?.visibility = View.VISIBLE
        viewModel.search(searchQuery.text.toString())
    }

    companion object {
        private const val MESSAGE_UPDATE_SEARCH = 123
    }
}
