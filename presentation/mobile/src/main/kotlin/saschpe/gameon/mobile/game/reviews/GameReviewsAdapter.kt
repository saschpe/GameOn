package saschpe.gameon.mobile.game.reviews

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
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameInfo.Review.Companion.NEUTRAL_REVIEW_THRESHOLD
import saschpe.gameon.data.core.model.GameInfo.Review.Companion.POSITIVE_REVIEW_THRESHOLD
import saschpe.gameon.mobile.R
import saschpe.gameon.common.R as CommonR

class GameReviewsAdapter(context: Context) :
    ListAdapter<GameReviewsAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_REVIEW -> ReviewViewHolder(
            inflater.inflate(R.layout.view_review_list_item, parent, false)
        )
        VIEW_TYPE_NO_RESULTS -> NoResultsViewHolder(
            inflater.inflate(R.layout.view_review_no_results, parent, false)
        )
        else -> throw Exception("Unsupported view type '$viewType'!")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ViewModel.ReviewViewModel -> (holder as ReviewViewHolder).bind(item)
            is ViewModel.NoResultsViewModel -> (holder as NoResultsViewHolder).bind()
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class ReviewViewModel(val store: String, val review: GameInfo.Review, val onClick: (() -> Unit)? = null) :
            ViewModel(VIEW_TYPE_REVIEW)

        class NoResultsViewModel : ViewModel(VIEW_TYPE_NO_RESULTS)
    }

    private class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val rating: TextView = view.findViewById(R.id.rating)
        private val store: TextView = view.findViewById(R.id.store)
        private val details: TextView = view.findViewById(R.id.priceAlertDescription)
        private val layout: View = view.findViewById(R.id.constraintLayout)

        fun bind(viewModel: ViewModel.ReviewViewModel) {
            if (viewModel.onClick != null) {
                layout.setOnClickListener { viewModel.onClick.invoke() }
                layout.isClickable = true
                layout.isFocusable = true
            } else {
                layout.isClickable = false
                layout.isFocusable = false
            }

            viewModel.review.let {
                details.text = details.resources.getQuantityString(
                    CommonR.plurals.review_detail,
                    it.total,
                    it.text,
                    it.total
                )

                val ratingColor = when {
                    it.perc_positive > POSITIVE_REVIEW_THRESHOLD -> colors.green
                    it.perc_positive > NEUTRAL_REVIEW_THRESHOLD -> colors.amber
                    else -> colors.red
                }

                rating.text = HtmlCompat.fromHtml(
                    rating.context.getString(
                        CommonR.string.colored_number_template,
                        it.perc_positive,
                        ratingColor
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            store.text = store.context.getString(CommonR.string.on_template, viewModel.store)
        }
    }

    private class NoResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() = Unit
    }

    companion object {
        private const val VIEW_TYPE_REVIEW = 1
        private const val VIEW_TYPE_NO_RESULTS = 2
    }
}
