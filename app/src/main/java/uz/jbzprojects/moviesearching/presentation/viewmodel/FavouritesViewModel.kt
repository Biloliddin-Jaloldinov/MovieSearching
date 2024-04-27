package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

interface FavouritesViewModel {
    val error: LiveData<String>
    val moviesLiveData: LiveData<List<FavouriteMovieEntity>>

    fun getMovies()
}