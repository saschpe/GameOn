package saschpe.gameon.mobile.game.prices


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.model.GamePrice
import saschpe.gameon.mobile.R

class GamePricesAdapter(
    context: Context
) : ListAdapter<GamePricesAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_PRICE -> PriceViewHolder(
                inflater.inflate(R.layout.view_price_list_item, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.PriceViewModel -> (holder as PriceViewHolder).bind(item)
        }

    sealed class ViewModel(val viewType: Int) {
        data class PriceViewModel(
            val price: GamePrice.Price,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_PRICE)
    }

    private class PriceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val store: TextView = view.findViewById(R.id.store)
        private val price: TextView = view.findViewById(R.id.price)
        private val drm: TextView = view.findViewById(R.id.drm)
        private val clickSurface: View = view

        fun bind(viewModel: ViewModel.PriceViewModel) {
            clickSurface.setOnClickListener { viewModel.onClick.invoke() }

            store.text = viewModel.price.shop.name
            price.text = viewModel.price.price_new.toString()
            drm.text = viewModel.price.drm.joinToString(separator = ", ")
        }
    }

    companion object {
        private const val VIEW_TYPE_PRICE = 1
    }
}