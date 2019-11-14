package saschpe.gamesale.mobile.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import saschpe.gamesale.common.recyclerview.DiffCallback
import saschpe.gamesale.data.core.model.GameInfo
import saschpe.gamesale.mobile.R

class GameReviewAdapter(
    context: Context
) : ListAdapter<GameReviewAdapter.ViewModel, RecyclerView.ViewHolder>(DiffCallback<ViewModel>()) {
    private val inflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int) = getItem(position).viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_REVIEW_CREATE -> CreateReviewViewHolder(
                inflater.inflate(R.layout.view_review_create, parent, false)
            )
            VIEW_TYPE_REVIEW -> ReviewViewHolder(
                inflater.inflate(R.layout.view_review, parent, false)
            )
            else -> throw Exception("Unsupported view type '$viewType'!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val item = getItem(position)) {
            is ViewModel.CreateReviewViewModel -> (holder as CreateReviewViewHolder).bind(item)
            is ViewModel.ReviewViewModel -> (holder as ReviewViewHolder).bind(item)
        }

    sealed class ViewModel(val viewType: Int) {
        data class CreateReviewViewModel(
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_REVIEW_CREATE)

        data class ReviewViewModel(
            val store: String,
            val review: GameInfo.Review,
            val onClick: () -> Unit = {}
        ) : ViewModel(VIEW_TYPE_REVIEW)
    }

    private class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val rating: TextView = view.findViewById(R.id.rating)
        private val store: TextView = view.findViewById(R.id.store)
        private val details: TextView = view.findViewById(R.id.details)
        private val clickSurface: View = view

        fun bind(viewModel: ViewModel.ReviewViewModel) {
            clickSurface.setOnClickListener { viewModel.onClick.invoke() }

            viewModel.review.let {
                details.text = details.resources.getQuantityString(
                    R.plurals.review_detail, it.total, it.text, it.total
                )

                rating.text = rating.context.getString(
                    R.string.colored_number_template,
                    it.perc_positive,
                    when {
                        it.perc_positive > POSITIVE_REVIEW_THRESHOLD ->
                            ContextCompat.getColor(rating.context, R.color.green)
                        it.perc_positive > NEUTRAL_REVIEW_THRESHOLD ->
                            ContextCompat.getColor(rating.context, R.color.amber)
                        else -> ContextCompat.getColor(rating.context, R.color.red)
                    }
                )
            }

            store.text = viewModel.store
        }
    }

    private class CreateReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val reviewButton: MaterialButton = view.findViewById(R.id.reviewButton)

        fun bind(viewModel: ViewModel.CreateReviewViewModel) {
            reviewButton.setOnClickListener { viewModel.onClick.invoke() }
        }
    }

    companion object {
        private const val NEUTRAL_REVIEW_THRESHOLD = 60
        private const val POSITIVE_REVIEW_THRESHOLD = 80
        private const val VIEW_TYPE_REVIEW_CREATE = 1
        private const val VIEW_TYPE_REVIEW = 2
    }
}