package uz.jbzprojects.moviesearching.network.data.maindata

import com.google.gson.annotations.SerializedName

data class AllMovies(
    val page: Int,
    @SerializedName("results") val movies: List<MovieData>,
    val total_pages: Int,
    val total_results: Int
)