package saschpe.gameon.mobile.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.mobile.R

class GameAdapter(
    context: Context
) : ListAdapter<GameAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_MAIN -> MainViewHolder(
                inflater.inflate(R.layout.view_game_main_card, parent, false)
            )
            VIEW_TYPE_REVIEWS -> ReviewsViewHolder(
                inflater.inflate(R.layout.view_game_reviews_card, parent, false)
            )
            VIEW_TYPE_INFO -> InfoViewHolder(
                inflater.inflate(R.layout.view_game_info_card, parent, false)
            )
            VIEW_TYPE_PRICES -> PricesViewHolder(
                inflater.inflate(R.layout.view_game_prices_card, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.MainViewModel -> (holder as MainViewHolder).bind(item)
            is ViewModel.ReviewsViewModel -> (holder as ReviewsViewHolder).bind(item)
            is ViewModel.InfoViewModel -> (holder as InfoViewHolder).bind(item)
            is ViewModel.PricesViewModel -> (holder as PricesViewHolder).bind(item)
        }

    sealed class ViewModel(val viewType: Int) {
        data class MainViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_MAIN)

        data class ReviewsViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_REVIEWS)

        data class InfoViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_INFO)

        data class PricesViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_PRICES)
    }

    private class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(viewModel: ViewModel.MainViewModel) {

        }
    }

    private class ReviewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(viewModel: ViewModel.ReviewsViewModel) {

        }
    }

    private class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(viewModel: ViewModel.InfoViewModel) {

        }
    }

    private class PricesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(viewModel: ViewModel.PricesViewModel) {

        }
    }

    companion object {
        private const val VIEW_TYPE_MAIN = 1
        private const val VIEW_TYPE_REVIEWS = 2
        private const val VIEW_TYPE_INFO = 3
        private const val VIEW_TYPE_PRICES = 4
    }
}