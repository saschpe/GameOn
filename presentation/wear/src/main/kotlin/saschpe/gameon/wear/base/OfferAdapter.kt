package saschpe.gameon.wear.base

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
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.common.Module.colors
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.DiffCallback
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Module.getGameInfoUseCase
import saschpe.gameon.wear.R

class OfferAdapter(
    context: Context
) : ListAdapter<OfferAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
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
            is ViewModel.NoResultsViewModel -> (holder as NoResultsViewHolder).bind(item)
            is ViewModel.OfferViewModel -> (holder as OfferViewHolder).bind(item)
        }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is OfferViewHolder -> holder.detach()
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class NoResultsViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_NO_RESULTS)

        data class OfferViewModel(
            val coroutineScope: CoroutineScope,
            val offer: Offer,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_OFFER)
    }

    private class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cover: ImageView = view.findViewById(R.id.cover)
        private val price: TextView = view.findViewById(R.id.price)
        private val rebate: TextView = view.findViewById(R.id.rebate)
        private val layout: View = view.findViewById(R.id.constraintLayout)
        private var gameInfoJob: Job? = null

        fun bind(viewModel: ViewModel.OfferViewModel) {
            layout.setOnClickListener { viewModel.onClick.invoke() }

            gameInfoJob = viewModel.coroutineScope.launch {
                when (val result = getGameInfoUseCase(viewModel.offer.plain)) {
                    is Result.Success<HashMap<String, GameInfo>> ->
                        result.data[viewModel.offer.plain]?.image?.let {
                            cover.load(it) {
                                placeholder(R.drawable.placeholder)
                                crossfade(true)
                            }
                        }
                    is Result.Error -> result.errorLogged()
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
        internal const val VIEW_TYPE_NO_RESULTS = 2
        internal const val VIEW_TYPE_OFFER = 3
    }
}