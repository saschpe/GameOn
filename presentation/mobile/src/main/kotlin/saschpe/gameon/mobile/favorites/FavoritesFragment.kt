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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        favoritesAdapter = FavoritesAdapter(requireContext())

        viewModel.getFavorites()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        recyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.favoritesLiveData.observe(this, Observer { favorites ->
            favoritesAdapter.submitList(favorites.map { favorite ->
                FavoritesAdapter.ViewModel.FavoriteViewModel(
                    favorite = favorite,
                    onClick = {
                        findNavController().navigate(
                            R.id.action_favoritesFragment_to_gameFragment,
                            bundleOf(
                                GameFragment.ARG_PLAIN to favorite.plain,
                                GameFragment.ARG_TITLE to favorite.title
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
            R.id.helpFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_helpFragment)
            R.id.settingsFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_settingsFragment)
        }
        return false
    }
}
