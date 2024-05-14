package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData

interface MoviesByGenreViewModel {
    val error: LiveData<String>
    val moviesLiveData: LiveData<List<MovieData>>

    fun getMoviesByGenre(genre : String)
}