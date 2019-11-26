package saschpe.gameon.mobile.game.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import saschpe.gameon.common.recyclerview.DiffCallback
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.mobile.R

class GameReviewsAdapter(
    context: Context
) : ListAdapter<GameReviewsAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
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
        }
    }

    sealed class ViewModel(val viewType: Int) {
        data class ReviewViewModel(
            val store: String,
            val review: GameInfo.Review,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_REVIEW)

        class NoResultsViewModel : ViewModel(VIEW_TYPE_NO_RESULTS)
    }

    private class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val rating: TextView = view.findViewById(R.id.rating)
        private val store: TextView = view.findViewById(R.id.store)
        private val details: TextView = view.findViewById(R.id.details)
        private val clickSurface: View = view

        init {
            if (AMBER_COLOR_INT == null) {
                AMBER_COLOR_INT = ContextCompat.getColor(view.context, R.color.amber)
            }
            if (GREEN_COLOR_INT == null) {
                GREEN_COLOR_INT = ContextCompat.getColor(view.context, R.color.green)
            }
            if (RED_COLOR_INT == null) {
                RED_COLOR_INT = ContextCompat.getColor(view.context, R.color.red)
            }
        }

        fun bind(viewModel: ViewModel.ReviewViewModel) {
            clickSurface.setOnClickListener { viewModel.onClick.invoke() }

            viewModel.review.let {
                details.text = details.resources.getQuantityString(
                    R.plurals.review_detail, it.total, it.text, it.total
                )

                val ratingColorInt = when {
                    it.perc_positive > POSITIVE_REVIEW_THRESHOLD -> GREEN_COLOR_INT
                    it.perc_positive > NEUTRAL_REVIEW_THRESHOLD -> AMBER_COLOR_INT
                    else -> RED_COLOR_INT
                }

                rating.text = HtmlCompat.fromHtml(
                    rating.context.getString(
                        R.string.colored_number_template,
                        it.perc_positive,
                        ratingColorInt
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            store.text =  store.context.getString(R.string.on_template, viewModel.store)
        }

        companion object {
            private var AMBER_COLOR_INT: Int? = null
            private var GREEN_COLOR_INT: Int? = null
            private var RED_COLOR_INT: Int? = null
        }
    }

    private class NoResultsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val NEUTRAL_REVIEW_THRESHOLD = 60
        private const val POSITIVE_REVIEW_THRESHOLD = 80
        private const val VIEW_TYPE_REVIEW = 1
        private const val VIEW_TYPE_NO_RESULTS = 2
    }
}