package saschpe.gameon.mobile.favorites

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
import saschpe.gameon.common.content.hasScreenWidth
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val gridLayoutSpanCount
        get() = when {
            requireContext().hasScreenWidth(720) -> 3
            requireContext().hasScreenWidth(600) -> 2
            else -> 1
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        favoritesAdapter = FavoritesAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        recyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = GridLayoutManager(context, gridLayoutSpanCount)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.favoritesLiveData.observe(this, Observer { favorites ->
            val viewModels = if (favorites.isNotEmpty()) {
                favorites.map { favorite ->
                    FavoritesAdapter.ViewModel.FavoriteViewModel(
                        favorite = favorite,
                        onClick = {
                            findNavController().navigate(
                                R.id.action_favoritesFragment_to_gameFragment,
                                bundleOf(GameFragment.ARG_PLAIN to favorite.plain)
                            )
                        }
                    )
                }
            } else {
                listOf(FavoritesAdapter.ViewModel.NoResultViewModel())
            }

            favoritesAdapter.submitList(viewModels)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_home, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.helpFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_helpFragment)
            R.id.settingsFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_settingsFragment)
        }
        return false
    }
}
