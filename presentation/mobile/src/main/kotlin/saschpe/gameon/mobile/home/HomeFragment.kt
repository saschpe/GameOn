package saschpe.gameon.mobile.home

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
import saschpe.gameon.common.app.appNameTitle
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var offerAdapter: OfferAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        offerAdapter = OfferAdapter(requireContext())
        viewModel.getDeals()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        viewModel.dealLiveData.observe(this, Observer { deals ->
            progressBar.visibility = View.GONE
            offerAdapter.submitList(deals.map { deal ->
                OfferAdapter.ViewModel.OfferViewModel(
                    offer = deal,
                    onClick = {
                        findNavController().navigate(
                            R.id.action_homeFragment_to_gameFragment,
                            bundleOf(
                                GameFragment.ARG_PLAIN to deal.plain,
                                GameFragment.ARG_TITLE to deal.title
                            )
                        )
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

        searchQuery.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).appNameTitle(appName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_home, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.helpFragment -> findNavController().navigate(R.id.action_homeFragment_to_helpFragment)
            R.id.settingsFragment -> findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        return false
    }
}