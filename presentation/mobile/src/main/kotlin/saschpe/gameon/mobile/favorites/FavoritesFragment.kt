package saschpe.gameon.mobile.favorites

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saschpe.gameon.common.content.hasScreenWidth
import saschpe.gameon.common.content.sharedPreferences
import saschpe.gameon.common.recyclerview.SpacingItemDecoration
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment
import saschpe.log4k.Log

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var navController: NavController
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
        navController = findNavController()
        setupWithNavController(toolbar, navController)
        toolbar.inflateMenu(R.menu.menu_favorites)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.help -> navController.navigate(R.id.action_favorites_to_help)
                R.id.view_comfy -> updateGridLayout(GRID_LAYOUT_SPAN_COUNT_INCREMENT_ONE)
                R.id.view_module -> updateGridLayout(GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE)
                R.id.settings -> navController.navigate(R.id.action_favorites_to_settings)
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

        viewLifecycleOwner.lifecycleScope.launch {
            updateGridLayout(withContext(Dispatchers.IO) {
                requireContext().sharedPreferences.getInt(
                    PREF_GRID_LAYOUT_SPAN_COUNT_INCREMENT, GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE
                )
            })
        }

        recyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(SpacingItemDecoration(context, R.dimen.recycler_spacing))
            setHasFixedSize(true)
        }

        var viewModels: MutableList<FavoritesAdapter.ViewModel>? = null

        viewModel.favoritesLiveData.observe(viewLifecycleOwner, Observer { favorites ->
            viewModels = if (favorites.isNotEmpty()) {
                favorites.map { favorite ->
                    FavoritesAdapter.ViewModel.FavoriteViewModel(
                        favorite = favorite,
                        onClick = {
                            navController.navigate(
                                R.id.action_favorites_to_game,
                                bundleOf(GameFragment.ARG_PLAIN to favorite.plain)
                            )
                        }
                    )
                }.toMutableList()
            } else {
                mutableListOf(FavoritesAdapter.ViewModel.NoResultViewModel(
                    onClick = {
                        navController.navigate(R.id.action_favorites_to_search)
                    }
                ))
            }

            favoritesAdapter.submitList(viewModels)
        })

        fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
            val tmp = this[index1] // 'this' corresponds to the list
            this[index1] = this[index2]
            this[index2] = tmp
        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos: Int = viewHolder.adapterPosition
                val toPos: Int = target.adapterPosition
                Log.debug("XXX $fromPos to $toPos in ${viewModels?.map { (it as FavoritesAdapter.ViewModel.FavoriteViewModel).favorite.plain }}")
                viewModels?.swap(fromPos, toPos)
                Log.debug("XXX $fromPos to $toPos in ${viewModels?.map { (it as FavoritesAdapter.ViewModel.FavoriteViewModel).favorite.plain }}")
                favoritesAdapter.submitList(viewModels)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Favorites", "FavoritesFragment")
        viewModel.getFavorites()
    }

    override fun onStop() {
        requireContext().sharedPreferences.edit {
            putInt(PREF_GRID_LAYOUT_SPAN_COUNT_INCREMENT, gridLayoutSpanCountIncrement)
        }
        super.onStop()
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

    companion object {
        private const val GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE = 0
        private const val GRID_LAYOUT_SPAN_COUNT_INCREMENT_ONE = 1
        private const val PREF_GRID_LAYOUT_SPAN_COUNT_INCREMENT = "grid_layout_span_count_increment"
    }
}
