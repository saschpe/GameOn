package saschpe.gameon.mobile.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.common.Module.colors
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.domain.Module.getGameInfoUseCase
import saschpe.gameon.domain.Module.getGameOverviewUseCase
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
        private val cover: ImageView = view.findViewById(R.id.cover)
        private val layout: View = view.findViewById(R.id.constraintLayout)
        private val price: TextView = view.findViewById(R.id.price)
        private var gameInfoJob: Job? = null
        private var gameOverviewJob: Job? = null

        fun bind(viewModel: ViewModel.FavoriteViewModel) {
            layout.setOnClickListener { viewModel.onClick.invoke() }

            val plain = viewModel.favorite.plain
            gameInfoJob = GlobalScope.launch(Dispatchers.Main) {
                when (val result = getGameInfoUseCase(plain)) {
                    is Result.Success<HashMap<String, GameInfo>> ->
                        result.data[plain]?.let { gameInfo ->
                            cover.load(gameInfo.image) {
                                placeholder(R.drawable.placeholder)
                                crossfade(true)
                            }
                        }
                    is Result.Error -> throw result.throwable
                }
            }
            gameOverviewJob = GlobalScope.launch(Dispatchers.Main) {
                when (val result = getGameOverviewUseCase(plain)) {
                    is Result.Success<HashMap<String, GameOverview>> ->
                        result.data[plain]?.let { gameOverview ->
                            gameOverview.price?.let { gamePrice ->
                                price.visibility = View.VISIBLE
                                if (gamePrice.cut > 0f) {
                                    price.text = HtmlCompat.fromHtml(
                                        price.context.getString(
                                            R.string.formatted_price_colored_template,
                                            gamePrice.price_formatted,
                                            colors.green
                                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                                    )
                                } else {
                                    price.text = HtmlCompat.fromHtml(
                                        gamePrice.price_formatted, HtmlCompat.FROM_HTML_MODE_LEGACY
                                    )
                                }
                            }
                        }
                    is Result.Error -> throw result.throwable
                }
            }
        }

        fun detach() {
            gameInfoJob?.cancel()
            gameOverviewJob?.cancel()
        }
    }

    private class NoResultViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        internal const val VIEW_TYPE_FAVORITE = 1
        internal const val VIEW_TYPE_NO_RESULT = 2
    }
}


