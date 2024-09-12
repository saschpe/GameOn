package saschpe.gameon.mobile.game.prices

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import saschpe.gameon.common.Module.colors
import saschpe.gameon.common.base.recyclerview.DiffCallback
import saschpe.gameon.data.core.model.GamePrice
import saschpe.gameon.data.core.model.GamePrice.Companion.GOOD_PRICE_CUT_THRESHOLD
import saschpe.gameon.mobile.R
import saschpe.gameon.common.R as CommonR

class GamePricesAdapter(context: Context) : ListAdapter<GamePricesAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_PRICE -> PriceViewHolder(
            inflater.inflate(R.layout.view_price_list_item, parent, false)
        )
        else -> throw Exception("Unsupported view type '$viewType'!")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (val item = getItem(position)) {
        is ViewModel.PriceViewModel -> (holder as PriceViewHolder).bind(item)
    }

    sealed class ViewModel(val viewType: Int) {
        data class PriceViewModel(val price: GamePrice.Price, val onClick: () -> Unit = {}) : ViewModel(VIEW_TYPE_PRICE)
    }

    private class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val store: TextView = view.findViewById(R.id.store)
        private val price: TextView = view.findViewById(R.id.price)
        private val drm: TextView = view.findViewById(R.id.drm)
        private val layout: View = view.findViewById(R.id.constraintLayout)

        fun bind(viewModel: ViewModel.PriceViewModel) {
            layout.setOnClickListener { viewModel.onClick.invoke() }

            val priceColorInt = if (viewModel.price.price_cut > GOOD_PRICE_CUT_THRESHOLD) {
                colors.green
            } else {
                colors.onSurface
            }

            store.text = viewModel.price.shop.name
            price.text = HtmlCompat.fromHtml(
                price.context.getString(
                    CommonR.string.price_colored_template,
                    viewModel.price.price_new,
                    priceColorInt
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            drm.text = viewModel.price.drm.joinToString(separator = ", ")
        }
    }

    companion object {
        private const val VIEW_TYPE_PRICE = 1
    }
}
