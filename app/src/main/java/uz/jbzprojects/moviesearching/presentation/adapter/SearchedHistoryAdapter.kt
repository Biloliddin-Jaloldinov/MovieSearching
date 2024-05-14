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
import uz.jbzprojects.moviesearching.databinding.ItemSearchHistoryBinding
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity

class SearchedHistoryAdapter : ListAdapter<SearchingHistoryEntity, SearchedHistoryAdapter.Holder>(Comparator()) {

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setItemClickListener(block: (String) -> Unit) {
        onItemClickListener = block
    }

    private var onItemLongClickListener: ((SearchingHistoryEntity) -> Unit)? = null
    fun setItemLongClickListener(block: (SearchingHistoryEntity) -> Unit) {
        onItemLongClickListener = block
    }


    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemSearchHistoryBinding.bind(view)


        fun bind(item: SearchingHistoryEntity) = with(binding) {
            if (item.searchedText.length > 25) {
                searchedText.text = item.searchedText.substring(0, 25) + " ..."
            } else {
                searchedText.text = item.searchedText
            }
            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.searchedText)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(item)
                true
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<SearchingHistoryEntity>() {
        override fun areItemsTheSame(oldItem: SearchingHistoryEntity, newItem: SearchingHistoryEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SearchingHistoryEntity, newItem: SearchingHistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))


}