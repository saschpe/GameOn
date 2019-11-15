package saschpe.gameon.mobile.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment
import saschpe.gameon.mobile.home.OfferAdapter

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var offerAdapter: OfferAdapter

    @SuppressLint("HandlerLeak")
    private val delayedSearchHandler: Handler = object : Handler() {
        override fun handleMessage(message: Message) {
            if (message.what == MESSAGE_UPDATE_SEARCH) {
                updateSearch()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        offerAdapter = OfferAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        viewModel.searchLiveData.observe(this, Observer { offers ->
            progressBar.visibility = View.GONE
            offerAdapter.submitList(offers.map { offer ->
                OfferAdapter.ViewModel.OfferViewModel(
                    offer = offer,
                    onClick = {
                        findNavController().navigate(
                            R.id.action_homeFragment_to_gameFragment,
                            bundleOf(
                                GameFragment.ARG_PLAIN to offer.plain,
                                GameFragment.ARG_TITLE to offer.title
                            )
                        )
                    }
                )
            })
        })

        searchQuery.doAfterTextChanged { text ->
            if (text?.count() ?: 0 > 2) {
                delayedSearchHandler.removeMessages(MESSAGE_UPDATE_SEARCH)
                delayedSearchHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_SEARCH, 300)
            }
        }
    }

    private fun updateSearch() {
        progressBar.visibility = View.VISIBLE
        viewModel.search(searchQuery.text.toString())
    }

    companion object {
        private const val MESSAGE_UPDATE_SEARCH = 123
    }
}