package saschpe.gameon.mobile.favorites

import android.os.Bundle
import android.view.View
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
        favoritesAdapter = FavoritesAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(toolbar, findNavController())
        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.helpFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_helpFragment)
                R.id.settingsFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_settingsFragment)
            }
            true
        }

        recyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = GridLayoutManager(context, gridLayoutSpanCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        when (favoritesAdapter.getItemViewType(position)) {
                            FavoritesAdapter.VIEW_TYPE_NO_RESULT -> gridLayoutSpanCount
                            else -> 1
                        }
                }
            }
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        viewModel.favoritesLiveData.observe(viewLifecycleOwner, Observer { favorites ->
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
}
