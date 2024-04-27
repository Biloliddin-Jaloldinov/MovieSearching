package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

interface MovieInfoViewModel {
    val error: LiveData<String>
    val movieLiveData: LiveData<OneMovieData>

    fun getMovieByID(movieID: Int)

    fun addMovieToFavourite(movieData: FavouriteMovieEntity)
    fun removeMovieFromFavourites(movieID: Int)
    fun checkMovieToFavourite(movieID: Int) : Boolean
}