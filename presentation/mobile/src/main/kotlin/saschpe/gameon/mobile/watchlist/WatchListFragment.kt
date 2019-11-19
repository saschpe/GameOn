package saschpe.gameon.mobile.watchlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment

class WatchListFragment : Fragment(R.layout.fragment_watchlist) {
    private val viewModel: WatchListViewModel by viewModels()
    private lateinit var watchAdapter: WatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        watchAdapter = WatchAdapter(requireContext())

        viewModel.getWatches()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        recyclerView.apply {
            adapter = watchAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.watchesLiveData.observe(this, Observer { watchList ->
            watchAdapter.submitList(watchList.map { watch ->
                WatchAdapter.ViewModel.WatchViewModel(
                    watch = watch,
                    onClick = {
                        findNavController().navigate(
                            R.id.action_watchListFragment_to_gameFragment,
                            bundleOf(
                                GameFragment.ARG_PLAIN to watch.plain,
                                GameFragment.ARG_TITLE to watch.title
                            )
                        )
                    }
                )
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_home, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.helpFragment -> findNavController().navigate(R.id.action_watchListFragment_to_helpFragment)
            R.id.settingsFragment -> findNavController().navigate(R.id.action_watchListFragment_to_settingsFragment)
        }
        return false
    }
}
