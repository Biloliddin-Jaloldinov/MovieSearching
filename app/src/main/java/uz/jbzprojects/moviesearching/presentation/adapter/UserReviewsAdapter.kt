package uz.jbzprojects.moviesearching.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ItemReviewBinding
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity

class UserReviewsAdapter : ListAdapter<ReviewEntity, UserReviewsAdapter.Holder>(Comparator()) {

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setItemClickListener(block: (Int) -> Unit) {
        onItemClickListener = block
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemReviewBinding.bind(view)

        fun bind(item: ReviewEntity) = with(binding) {
            textUerName.text = item.movieTitle
            textDate.text = item.date
            textComment.text = item.comment
            Picasso.get().load("https://image.tmdb.org/t/p/w500${item.movieImage}").into(imgUser)
            textRating.text = item.rating

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.movieID)
            }
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