package saschpe.gamesale.mobile.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import saschpe.gamesale.common.recyclerview.DiffCallback
import saschpe.gamesale.data.core.model.Offer
import saschpe.gamesale.mobile.R

class OfferAdapter(
    context: Context
) : ListAdapter<OfferAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_OFFER -> OfferViewHolder(
                inflater.inflate(R.layout.view_search_offer, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.OfferViewModel -> (holder as OfferViewHolder).bind(item)
        }

    sealed class ViewModel(val viewType: Int) {
        data class OfferViewModel(
            val offer: Offer,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_OFFER)
    }

    private class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.image)
        private val title: TextView = view.findViewById(R.id.title)
        private val clickSurface: View = view

        fun bind(viewModel: ViewModel.OfferViewModel) {
            clickSurface.setOnClickListener { viewModel.onClick.invoke() }
            title.text = viewModel.offer.title
        }
    }

    companion object {
        private const val VIEW_TYPE_OFFER = 1
    }
}