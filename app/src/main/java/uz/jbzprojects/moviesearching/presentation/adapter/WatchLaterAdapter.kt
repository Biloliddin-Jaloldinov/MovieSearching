package uz.jbzprojects.moviesearching.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ItemFavouritesMovieBinding
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity

class WatchLaterAdapter : ListAdapter<WatchLaterEntity, WatchLaterAdapter.Holder>(Comparator()) {

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setItemClickListener(block: (Int) -> Unit) {
        onItemClickListener = block
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFavouritesMovieBinding.bind(view)

        fun bind(item: WatchLaterEntity) = with(binding) {
            if (item.title.length > 25) {
                textMovieName.text = item.title.substring(0, 25) + " ..."
            } else {
                textMovieName.text = item.title
            }
            textMovieDate.text = item.date
            binding.imageIsFavourite.setImageResource(R.drawable.icon_bookmark_selected)
            Picasso.get().load("https://image.tmdb.org/t/p/w500${item.image}").into(imageMovie)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.movieId)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<WatchLaterEntity>() {
        override fun areItemsTheSame(oldItem: WatchLaterEntity, newItem: WatchLaterEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WatchLaterEntity, newItem: WatchLaterEntity): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_favourites_movie, parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))


}