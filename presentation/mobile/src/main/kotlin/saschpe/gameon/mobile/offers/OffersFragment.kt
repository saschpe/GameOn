package saschpe.gameon.mobile.offers

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
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gameon.common.app.appNameTitle
import saschpe.gameon.common.content.hasScreenWidth
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.OfferAdapter
import saschpe.gameon.mobile.game.GameFragment

class OffersFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: OffersViewModel by viewModels()
    private lateinit var offerAdapter: OfferAdapter
    private val gridLayoutSpanCount
        get() = when {
            requireContext().hasScreenWidth(720) -> 4
            requireContext().hasScreenWidth(600) -> 3
            requireContext().hasScreenWidth(360) -> 2
            else -> 1
        }

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

        recyclerView.apply {
            adapter = offerAdapter
            layoutManager = GridLayoutManager(context, gridLayoutSpanCount)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        searchQuery.setOnClickListener {
            findNavController().navigate(R.id.action_offersFragment_to_searchFragment)
        }

        viewModel.dealLiveData.observe(this, Observer { deals ->
            progressBar.visibility = View.GONE
            offerAdapter.submitList(deals.map { deal ->
                OfferAdapter.ViewModel.OfferViewModel(
                    offer = deal,
                    onClick = {
                        findNavController().navigate(
                            R.id.action_offersFragment_to_gameFragment,
                            bundleOf(GameFragment.ARG_PLAIN to deal.plain)
                        )
                    }
                )
            })
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).appNameTitle(appName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_home, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.helpFragment -> findNavController().navigate(R.id.action_offersFragment_to_helpFragment)
            R.id.settingsFragment -> findNavController().navigate(R.id.action_offersFragment_to_settingsFragment)
        }
        return false
    }
}