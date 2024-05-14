package uz.jbzprojects.moviesearching.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ItemGenreBinding
import uz.jbzprojects.moviesearching.network.data.genresData.GenreData

class GenresAdapter : ListAdapter<GenreData, GenresAdapter.Holder>(Comparator()) {

    private var onItemClickListener: ((GenreData) -> Unit)? = null
    fun setItemClickListener(block: (GenreData) -> Unit) {
        onItemClickListener = block
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemGenreBinding.bind(view)

        fun bind(item: GenreData) = with(binding) {

            categoryName.text = item.name.capitalize()

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<GenreData>() {
        override fun areItemsTheSame(oldItem: GenreData, newItem: GenreData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GenreData, newItem: GenreData): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))


}