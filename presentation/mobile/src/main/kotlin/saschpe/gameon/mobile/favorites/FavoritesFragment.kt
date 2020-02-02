package saschpe.gameon.mobile.favorites

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gameon.common.content.hasScreenWidth
import saschpe.gameon.common.content.sharedPreferences
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private var gridLayoutSpanCountIncrement = GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE
    private val gridLayoutSpanCount
        get() = gridLayoutSpanCountIncrement + when {
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
        toolbar.inflateMenu(R.menu.menu_favorites)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.view_module -> updateGridLayout(GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE)
                R.id.view_comfy -> updateGridLayout(GRID_LAYOUT_SPAN_COUNT_INCREMENT_ONE)
                R.id.helpFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_helpFragment)
                R.id.settingsFragment -> findNavController().navigate(R.id.action_favoritesFragment_to_settingsFragment)
            }
            true
        }

        gridLayoutManager = GridLayoutManager(context, gridLayoutSpanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    when (favoritesAdapter.getItemViewType(position)) {
                        FavoritesAdapter.VIEW_TYPE_NO_RESULT -> gridLayoutSpanCount
                        else -> 1
                    }
            }
        }

        updateGridLayout(
            requireContext().sharedPreferences.getInt(
                PREF_GRID_LAYOUT_SPAN_COUNT_INCREMENT, GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE
            )
        )

        recyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = gridLayoutManager
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

    private fun updateGridLayout(spanCountIncrement: Int) {
        gridLayoutSpanCountIncrement = spanCountIncrement
        gridLayoutManager.requestSimpleAnimationsInNextLayout()
        gridLayoutManager.spanCount = gridLayoutSpanCount
        toolbar.menu.apply {
            findItem(R.id.view_module).isVisible =
                gridLayoutSpanCountIncrement == GRID_LAYOUT_SPAN_COUNT_INCREMENT_ONE
            findItem(R.id.view_comfy).isVisible =
                gridLayoutSpanCountIncrement == GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    override fun onStop() {
        requireContext().sharedPreferences.edit {
            putInt(PREF_GRID_LAYOUT_SPAN_COUNT_INCREMENT, gridLayoutSpanCountIncrement)
        }
        super.onStop()
    }

    companion object {
        private const val GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE = 0
        private const val GRID_LAYOUT_SPAN_COUNT_INCREMENT_ONE = 1
        private const val PREF_GRID_LAYOUT_SPAN_COUNT_INCREMENT = "grid_layout_span_count_increment"
    }
}
