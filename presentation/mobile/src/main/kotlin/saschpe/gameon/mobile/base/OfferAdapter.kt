package saschpe.gameon.mobile.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.common.Module.colors
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Module.getGameInfoUseCase
import saschpe.gameon.mobile.R

class OfferAdapter(
    context: Context
) : ListAdapter<OfferAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_ADVERTISEMENT -> AdvertisementViewHolder(
                inflater.inflate(R.layout.view_offer_advertisement, parent, false)
            )
            VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(
                inflater.inflate(R.layout.view_offer_no_results, parent, false)
            )
            VIEW_TYPE_OFFER -> OfferViewHolder(
                inflater.inflate(R.layout.view_offer_card, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.AdvertisementViewModel -> (holder as AdvertisementViewHolder).bind(item)
            is ViewModel.NoResultsViewModel -> (holder as NoResultsViewHolder).bind(item)
            is ViewModel.OfferViewModel -> (holder as OfferViewHolder).bind(item)
        }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is AdvertisementViewHolder -> holder.detach()
            is OfferViewHolder -> holder.detach()
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class AdvertisementViewModel(
            val nativeAd: UnifiedNativeAd
        ) : ViewModel(VIEW_TYPE_ADVERTISEMENT)

        data class NoResultsViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_NO_RESULTS)

        data class OfferViewModel(
            val coroutineScope: CoroutineScope,
            val offer: Offer,
            val onClick: (transitionView: ImageView) -> Unit = {}
        ) : ViewModel(VIEW_TYPE_OFFER)
    }

    private class AdvertisementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nativeAdView: UnifiedNativeAdView = view.findViewById(R.id.nativeAdView)
        private val icon: ImageView = view.findViewById(R.id.icon)
        private val headline: MaterialTextView = view.findViewById(R.id.headline)
        private val secondary: MaterialTextView = view.findViewById(R.id.secondary)
        private val rating: RatingBar = view.findViewById(R.id.rating)
        private val body: MaterialTextView = view.findViewById(R.id.body)
        private val callToAction: MaterialButton = view.findViewById(R.id.callToAction)

        fun bind(viewModel: ViewModel.AdvertisementViewModel) {
            val nativeAd = viewModel.nativeAd

            nativeAdView.setNativeAd(nativeAd)
            nativeAdView.headlineView = headline.apply { text = nativeAd.headline }
            nativeAdView.bodyView = body.apply { text = nativeAd.body }
            nativeAdView.callToActionView = callToAction.apply { text = nativeAd.callToAction }

            if (nativeAd.icon != null) {
                icon.visibility = View.VISIBLE
                nativeAdView.iconView = icon.apply { setImageDrawable(nativeAd.icon.drawable) }
            } else {
                icon.visibility = View.INVISIBLE
            }

            if (nativeAd.starRating != null && nativeAd.starRating > 0) {
                secondary.visibility = View.INVISIBLE
                rating.visibility = View.VISIBLE
                nativeAdView.starRatingView = rating.apply {
                    rating = nativeAd.starRating.toFloat()
                    max = 5
                }
            } else {
                when {
                    nativeAd.store?.isNotEmpty() == true && nativeAd.advertiser?.isEmpty() == true ->
                        nativeAdView.storeView = secondary.apply { text = nativeAd.store }
                    nativeAd.advertiser?.isNotEmpty() == true ->
                        nativeAdView.advertiserView = secondary.apply { text = nativeAd.advertiser }
                    else -> secondary.text = ""
                }
                secondary.visibility = View.VISIBLE
                rating.visibility = View.INVISIBLE
            }
        }

        fun detach() = nativeAdView.destroy()
    }

    private class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cover: ImageView = view.findViewById(R.id.cover)
        private val price: TextView = view.findViewById(R.id.price)
        private val rebate: TextView = view.findViewById(R.id.rebate)
        private val layout: View = view.findViewById(R.id.constraintLayout)
        private var gameInfoJob: Job? = null

        fun bind(viewModel: ViewModel.OfferViewModel) {
            layout.setOnClickListener { viewModel.onClick.invoke(cover) }

            gameInfoJob = viewModel.coroutineScope.launch {
                when (val result = getGameInfoUseCase(viewModel.offer.plain)) {
                    is Result.Success<HashMap<String, GameInfo>> ->
                        result.data[viewModel.offer.plain]?.image?.let {
                            cover.load(it) {
                                placeholder(R.drawable.placeholder)
                                crossfade(true)
                            }
                        }
                    is Result.Error -> throw result.throwable
                }
            }

            viewModel.offer.run {
                if (price_cut > 0f) {
                    price.text = HtmlCompat.fromHtml(
                        price.context.getString(
                            R.string.price_colored_template, price_new, colors.green
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

                    rebate.text = HtmlCompat.fromHtml(
                        price.context.getString(
                            R.string.rebate_colored_template, price_cut, colors.red
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    rebate.visibility = View.VISIBLE
                } else {
                    price.text = price.context.getString(R.string.price_template, price_new)
                }
            }
        }

        fun detach() {
            gameInfoJob?.cancel()
        }
    }

    private class NoResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val clearButton: MaterialButton = view.findViewById(R.id.clearButton)

        fun bind(viewModel: ViewModel.NoResultsViewModel) {
            clearButton.setOnClickListener { viewModel.onClick.invoke() }
        }
    }

    companion object {
        internal const val VIEW_TYPE_ADVERTISEMENT = 1
        internal const val VIEW_TYPE_NO_RESULTS = 2
        internal const val VIEW_TYPE_OFFER = 3
    }
}