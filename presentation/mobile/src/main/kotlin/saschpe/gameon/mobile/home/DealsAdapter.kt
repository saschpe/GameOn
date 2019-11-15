package saschpe.gameon.mobile.home

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.Offer
import saschpe.gameon.domain.Model.getGameInfoUseCase
import saschpe.gameon.mobile.R
import kotlin.math.roundToInt

class DealsAdapter(
    context: Context
) : ListAdapter<DealsAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_DEAL -> DealViewHolder(
                inflater.inflate(R.layout.view_deal_card, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.DealViewModel -> (holder as DealViewHolder).bind(item)
        }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is DealViewHolder -> holder.detach()
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class DealViewModel(
            val deal: Offer,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_DEAL)
    }

    private class DealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

        fun bind(viewModel: ViewModel.DealViewModel) {
            clickSurface.setOnClickListener { viewModel.onClick.invoke() }
            title.text = viewModel.deal.title

            // TODO: Currency
            pricing.text = HtmlCompat.fromHtml(
                clickSurface.context.getString(
                    R.string.pricing_template,
                    viewModel.deal.price_new,
                    viewModel.deal.shop.name,
                    viewModel.deal.price_cut.roundToInt(),
                    GREEN_COLOR_INT, RED_COLOR_INT
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            gameInfoJob = GlobalScope.launch(Dispatchers.IO) {
                when (val result = getGameInfoUseCase(viewModel.deal.plain)) {
                    is Result.Success<HashMap<String, GameInfo>> -> launch(Dispatchers.Main) {
                        result.data[viewModel.deal.plain]?.image?.let {
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

    companion object {
        private const val VIEW_TYPE_DEAL = 1
    }
}