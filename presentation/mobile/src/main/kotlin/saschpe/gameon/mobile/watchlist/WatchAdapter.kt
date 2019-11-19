package saschpe.gameon.mobile.watchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.model.Watch
import saschpe.gameon.mobile.R

class WatchAdapter(
    context: Context
) : ListAdapter<WatchAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_WATCH -> WatchViewHolder(
                inflater.inflate(R.layout.view_watch_card, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.WatchViewModel -> (holder as WatchViewHolder).bind(item)
        }

    sealed class ViewModel(val viewType: Int) {
        data class WatchViewModel(
            val watch: Watch,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_WATCH)
    }

    private class WatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(viewModel: ViewModel.WatchViewModel) {
            // TODO:
        }
    }

    companion object {
        private const val VIEW_TYPE_WATCH = 1
    }
}