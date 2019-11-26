package saschpe.gameon.mobile.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Module.getGameInfoUseCase
import saschpe.gameon.mobile.R
import kotlin.math.roundToInt

class OfferAdapter(
    context: Context
) : ListAdapter<OfferAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_OFFER -> OfferViewHolder(
                inflater.inflate(R.layout.view_offer_list_card, parent, false)
            )
            VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(
                inflater.inflate(R.layout.view_offer_no_results, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.OfferViewModel -> (holder as OfferViewHolder).bind(item)
            is ViewModel.NoResultsViewModel -> (holder as NoResultsViewHolder).bind(item)
        }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is OfferViewHolder -> holder.detach()
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class OfferViewModel(
            val offer: Offer,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_OFFER)

        data class NoResultsViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_NO_RESULTS)
    }

    private class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.image)
        private val title: TextView = view.findViewById(R.id.title)
        private val pricing: TextView = view.findViewById(R.id.pricing)
        private val clickSurface: View = view
        private var gameInfoJob: Job? = null

        init {
            if (GREEN_COLOR_INT == null) {
                GREEN_COLOR_INT = ContextCompat.getColor(view.context, R.color.green)
            }
            if (RED_COLOR_INT == null) {
                RED_COLOR_INT = ContextCompat.getColor(view.context, R.color.red)
            }
        }

        fun bind(viewModel: ViewModel.OfferViewModel) {
            clickSurface.setOnClickListener { viewModel.onClick.invoke() }
            title.text = viewModel.offer.title

            // TODO: Currency
            pricing.text = HtmlCompat.fromHtml(
                clickSurface.context.getString(
                    R.string.pricing_template,
                    viewModel.offer.price_new,
                    viewModel.offer.shop.name,
                    viewModel.offer.price_cut.roundToInt(),
                    GREEN_COLOR_INT,
                    RED_COLOR_INT
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            gameInfoJob = GlobalScope.launch(Dispatchers.IO) {
                when (val result = getGameInfoUseCase(viewModel.offer.plain)) {
                    is Result.Success<HashMap<String, GameInfo>> -> launch(Dispatchers.Main) {
                        result.data[viewModel.offer.plain]?.image?.let {
                            image.load(it) { crossfade(true) }
                        }
                    }
                    is Result.Error -> throw result.throwable
                }
            }
        }

        fun detach() {
            gameInfoJob?.cancel()
        }

        companion object {
            private var GREEN_COLOR_INT: Int? = null
            private var RED_COLOR_INT: Int? = null
        }
    }

    private class NoResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val clearButton: MaterialButton = view.findViewById(R.id.clearButton)

        fun bind(viewModel: ViewModel.NoResultsViewModel) {
            clearButton.setOnClickListener { viewModel.onClick.invoke() }
        }
    }

    companion object {
        private const val VIEW_TYPE_OFFER = 1
        private const val VIEW_TYPE_NO_RESULTS = 2
    }
}