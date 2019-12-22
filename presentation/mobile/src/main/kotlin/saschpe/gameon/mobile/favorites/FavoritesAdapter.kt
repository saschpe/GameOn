package saschpe.gameon.mobile.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.domain.Module
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
            VIEW_TYPE_NO_RESULT -> NoResultViewHolder(
                inflater.inflate(R.layout.view_favorite_no_results, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ViewModel.FavoriteViewModel -> (holder as FavoriteViewHolder).bind(item)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is FavoriteViewHolder -> holder.detach()
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class FavoriteViewModel(
            val favorite: Favorite,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_FAVORITE)

        class NoResultViewModel : ViewModel(VIEW_TYPE_NO_RESULT)
    }

    private class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.image)
        private val layout: View = view.findViewById(R.id.constraintLayout)
        private var gameInfoJob: Job? = null

        fun bind(viewModel: ViewModel.FavoriteViewModel) {
            layout.setOnClickListener { viewModel.onClick.invoke() }
            gameInfoJob = GlobalScope.launch {
                when (val result = Module.getGameInfoUseCase(viewModel.favorite.plain)) {
                    is Result.Success<HashMap<String, GameInfo>> ->
                        result.data[viewModel.favorite.plain]?.let { gameInfo ->
                            gameInfo.image.let { image.load(it) { crossfade(true) } }
                        }
                    is Result.Error -> throw result.throwable
                }
            }
        }

        fun detach() {
            gameInfoJob?.cancel()
        }
    }

    private class NoResultViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_FAVORITE = 1
        private const val VIEW_TYPE_NO_RESULT = 2
    }
}


