package saschpe.gameon.mobile.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.mobile.R

class FavoritesAdapter(
    context: Context
) : ListAdapter<FavoritesAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_FAVORITE -> FavoriteViewHolder(
                inflater.inflate(R.layout.view_favorite_card, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.FavoriteViewModel -> (holder as FavoriteViewHolder).bind(item)
        }

    sealed class ViewModel(val viewType: Int) {
        data class FavoriteViewModel(
            val favorite: Favorite,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_FAVORITE)
    }

    private class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(viewModel: ViewModel.FavoriteViewModel) {
            // TODO:
        }
    }

    companion object {
        private const val VIEW_TYPE_FAVORITE = 1
    }
}