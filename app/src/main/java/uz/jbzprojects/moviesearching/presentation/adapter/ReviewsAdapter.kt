package uz.jbzprojects.moviesearching.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ItemReviewBinding
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity

class ReviewsAdapter(private val isAdmin: Boolean) : ListAdapter<ReviewEntity, ReviewsAdapter.Holder>(Comparator()) {

    private var onDeleteButtonClickListener: ((ReviewEntity) -> Unit)? = null
    fun setDeleteButtonListener(block: (ReviewEntity) -> Unit) {
        onDeleteButtonClickListener = block
    }
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemReviewBinding.bind(view)

        fun bind(item: ReviewEntity) = with(binding) {
            if (isAdmin) {
                btnDeleteReview.visibility = View.VISIBLE
            } else {
                btnDeleteReview.visibility = View.GONE
            }
            btnDeleteReview.setOnClickListener {
                onDeleteButtonClickListener?.invoke(item)
            }
            textUerName.text = item.userName
            textDate.text = item.date
            textComment.text = item.comment
            imgUser.setImageResource(item.imageUser)
            textRating.text = item.rating

        }
    }

    class Comparator : DiffUtil.ItemCallback<ReviewEntity>() {
        override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))


}