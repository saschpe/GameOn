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
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.model.Offer
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

        fun bind(viewModel: ViewModel.DealViewModel) {
            clickSurface.setOnClickListener { viewModel.onClick.invoke() }
            title.text = viewModel.deal.title

            // TODO: Currency, template, image load

            val green = ContextCompat.getColor(clickSurface.context, R.color.green)
            val red = ContextCompat.getColor(clickSurface.context, R.color.red)

            pricing.text = HtmlCompat.fromHtml(
                clickSurface.context.getString(
                    R.string.pricing_template,
                    viewModel.deal.price_new,
                    viewModel.deal.shop.name,
                    viewModel.deal.price_cut.roundToInt(),
                    green, red
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    companion object {
        private const val VIEW_TYPE_DEAL = 1
    }
}