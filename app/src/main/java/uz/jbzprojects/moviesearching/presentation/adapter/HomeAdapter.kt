package uz.jbzprojects.moviesearching.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ItemMovieBinding
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData

class HomeAdapter : ListAdapter<MovieData, HomeAdapter.Holder>(Comparator()) {

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setItemClickListener(block: (Int) -> Unit) {
        onItemClickListener = block
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemMovieBinding.bind(view)

        fun bind(item: MovieData) = with(binding) {

            textMovieName.text = item.title
            textMovieYear.text = item.release_date
            textRating.text = (item.vote_average / 2f).toString().substring(0,3)
            ratingBarMovie.rating = (item.vote_average / 2f).toFloat()
            Log.d("TTT", "Img url$absoluteAdapterPosition: " + "https://image.tmdb.org/t/p/original" + item.overview)
            Picasso.get().load("https://image.tmdb.org/t/p/w500${item.backdrop_path}").into(imageMovie)
            itemView.setOnClickListener {
                Log.d("TTT","MainAdapter clicked")
                onItemClickListener?.invoke(item.id)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<MovieData>() {
        override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))


}