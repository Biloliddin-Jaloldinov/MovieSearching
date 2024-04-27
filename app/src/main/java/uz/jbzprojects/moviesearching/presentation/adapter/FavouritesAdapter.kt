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
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

class FavouritesAdapter : ListAdapter<FavouriteMovieEntity, FavouritesAdapter.Holder>(Comparator()) {

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setItemClickListener(block: (Int) -> Unit) {
        onItemClickListener = block
    }
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFavouritesMovieBinding.bind(view)

        fun bind(item: FavouriteMovieEntity) = with(binding) {

            textMovieName.text = item.title
            textMovieDate.text = item.date
            Picasso.get().load("https://image.tmdb.org/t/p/w500${item.image}").into(imageMovie)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.movieId)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<FavouriteMovieEntity>() {
        override fun areItemsTheSame(oldItem: FavouriteMovieEntity, newItem: FavouriteMovieEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FavouriteMovieEntity, newItem: FavouriteMovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_favourites_movie, parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))


}