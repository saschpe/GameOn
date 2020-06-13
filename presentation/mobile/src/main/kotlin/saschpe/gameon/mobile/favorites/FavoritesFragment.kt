package saschpe.gameon.mobile.favorites

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saschpe.gameon.common.base.content.hasScreenWidth
import saschpe.gameon.common.base.content.sharedPreferences
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.SpacingItemDecoration
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.NativeAdUnit
import saschpe.gameon.mobile.base.loadAdvertisement
import saschpe.gameon.mobile.game.GameFragment

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
    private var adViewModels: List<FavoritesAdapter.ViewModel.AdvertisementViewModel> = listOf()
    private var favoriteViewModels: List<FavoritesAdapter.ViewModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesAdapter = FavoritesAdapter(requireContext())

        loadAdvertisement(NativeAdUnit.Favorites) {
            adViewModels = listOf(FavoritesAdapter.ViewModel.AdvertisementViewModel(it))
            favoritesAdapter.submitList(adViewModels + favoriteViewModels)
        }
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
                        FavoritesAdapter.VIEW_TYPE_ADVERTISEMENT -> gridLayoutSpanCount
                        FavoritesAdapter.VIEW_TYPE_NO_RESULT -> gridLayoutSpanCount
                        else -> 1
                    }
            }
        }

        lifecycleScope.launch {
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

        viewModel.favoritesLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success<List<Favorite>> -> {
                    favoriteViewModels = when {
                        result.data.isNotEmpty() -> result.data.map { favorite ->
                            FavoritesAdapter.ViewModel.FavoriteViewModel(lifecycleScope, favorite) {
                                navController.navigate(
                                    R.id.action_favorites_to_game,
                                    bundleOf(GameFragment.ARG_PLAIN to favorite.plain)
                                )
                            }
                        }
                        else -> listOf(FavoritesAdapter.ViewModel.NoResultViewModel {
                            navController.navigate(R.id.action_favorites_to_search)
                        })
                    }
                    favoritesAdapter.submitList(adViewModels + favoriteViewModels)
                }
                is Result.Error -> {
                    result.errorLogged()
                    showSnackBarWithRetryAction(R.string.unable_to_load_favorites) {
                        viewModel.getFavorites()
                    }
                }
            }
        })
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

    private fun showSnackBarWithRetryAction(@StringRes resId: Int, retryCallback: () -> Unit) =
        Snackbar
            .make(coordinatorLayout, getString(resId), Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { retryCallback.invoke() }
            .show()

    companion object {
        private const val GRID_LAYOUT_SPAN_COUNT_INCREMENT_NONE = 0
        private const val GRID_LAYOUT_SPAN_COUNT_INCREMENT_ONE = 1
        private const val PREF_GRID_LAYOUT_SPAN_COUNT_INCREMENT = "grid_layout_span_count_increment"
    }
}
