package saschpe.gameon.common.recyclerview

import androidx.recyclerview.widget.DiffUtil

class DiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}
