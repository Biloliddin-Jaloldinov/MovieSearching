package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.genresData.GenreData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData

interface GenresViewModel {
    val error: LiveData<String>
    val moviesLiveData: LiveData<List<GenreData>>

    fun getMoviesGenres()
}