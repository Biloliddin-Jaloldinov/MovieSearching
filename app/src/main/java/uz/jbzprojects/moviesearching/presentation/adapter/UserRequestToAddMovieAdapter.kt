package uz.jbzprojects.moviesearching.presentation.adapter

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.app.App.Companion.context
import uz.jbzprojects.moviesearching.databinding.ItemFavouritesMovieBinding
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity

class UserRequestToAddMovieAdapter : ListAdapter<UserMovieRequestEntity, UserRequestToAddMovieAdapter.Holder>(Comparator()) {

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setItemClickListener(block: (Int) -> Unit) {
        onItemClickListener = block
    }

    private var onItemLongClickListener: ((UserMovieRequestEntity) -> Unit)? = null
    fun setItemLongClickListener(block: (UserMovieRequestEntity) -> Unit) {
        onItemLongClickListener = block
    }


    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFavouritesMovieBinding.bind(view)


        fun bind(item: UserMovieRequestEntity) = with(binding) {
            if (item.title.length > 25) {
                textMovieName.text = item.title.substring(0, 25) + " ..."
            } else {
                textMovieName.text = item.title
            }
            textMovieDate.text = item.date
            if (item.imageUrl.isEmpty()) {
                imageMovie.setImageResource(R.drawable.img_not_found)
            } else {
                val imageUri = Uri.parse(item.imageUrl)
                val contentResolver = context.contentResolver
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageMovie.setImageBitmap(bitmap)
            }
            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.movieId)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(item)
                true
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<UserMovieRequestEntity>() {
        override fun areItemsTheSame(oldItem: UserMovieRequestEntity, newItem: UserMovieRequestEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserMovieRequestEntity, newItem: UserMovieRequestEntity): Boolean {
            return oldItem.movieId == newItem.movieId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_favourites_movie, parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))


}