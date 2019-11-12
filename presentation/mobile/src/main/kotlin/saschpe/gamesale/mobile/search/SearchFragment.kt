package saschpe.gamesale.mobile.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gamesale.mobile.R

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()

    @SuppressLint("HandlerLeak")
    private val delayedSearchHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == MESSAGE_UPDATE_SEARCH) {
                updateSearch()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

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