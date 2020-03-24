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
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.*
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
            VIEW_TYPE_ADVERTISEMENT -> AdvertisementViewHolder(
                inflater.inflate(R.layout.view_favorite_advertisement, parent, false)
            )
            VIEW_TYPE_FAVORITE -> FavoriteViewHolder(
                inflater.inflate(R.layout.view_favorite_card, parent, false)
            )
            VIEW_TYPE_NO_RESULT -> NoResultViewHolder(
                inflater.inflate(R.layout.view_favorite_no_results, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.AdvertisementViewModel -> (holder as AdvertisementViewHolder).bind(item)
            is ViewModel.FavoriteViewModel -> (holder as FavoriteViewHolder).bind(item)
            is ViewModel.NoResultViewModel -> (holder as NoResultViewHolder).bind(item)
        }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is AdvertisementViewHolder -> holder.detach()
            is FavoriteViewHolder -> holder.detach()
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class AdvertisementViewModel(
            val nativeAd: UnifiedNativeAd
        ) : ViewModel(VIEW_TYPE_ADVERTISEMENT)

        data class FavoriteViewModel(
            val coroutineScope: CoroutineScope,
            val favorite: Favorite,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_FAVORITE)

        data class NoResultViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_NO_RESULT)
    }

    private class AdvertisementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nativeAdView: UnifiedNativeAdView = view.findViewById(R.id.nativeAdView)
        private val icon: ImageView = view.findViewById(R.id.icon)
        private val headline: MaterialTextView = view.findViewById(R.id.headline)
        private val body: MaterialTextView = view.findViewById(R.id.body)

        fun bind(viewModel: ViewModel.AdvertisementViewModel) {
            val nativeAd = viewModel.nativeAd

            nativeAdView.setNativeAd(nativeAd)
            nativeAdView.headlineView = headline.apply { text = nativeAd.headline }
            nativeAdView.bodyView = body.apply { text = nativeAd.body }

            if (nativeAd.icon != null) {
                icon.visibility = View.VISIBLE
                nativeAdView.iconView = icon.apply { setImageDrawable(nativeAd.icon.drawable) }
            } else {
                icon.visibility = View.INVISIBLE
            }
        }

        fun detach() = nativeAdView.destroy()
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
            gameInfoJob = viewModel.coroutineScope.launch(Dispatchers.Main) {
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

    private class NoResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val searchNow: MaterialButton = view.findViewById(R.id.searchNow)

        fun bind(viewModel: ViewModel.NoResultViewModel) {
            searchNow.setOnClickListener { viewModel.onClick.invoke() }
        }
    }

    companion object {
        internal const val VIEW_TYPE_ADVERTISEMENT = 1
        internal const val VIEW_TYPE_FAVORITE = 2
        internal const val VIEW_TYPE_NO_RESULT = 3
    }
}


