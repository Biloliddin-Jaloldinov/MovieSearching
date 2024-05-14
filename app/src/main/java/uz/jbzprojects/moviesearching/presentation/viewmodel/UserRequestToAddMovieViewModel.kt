package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity

interface UserRequestToAddMovieViewModel {
    val error: LiveData<String>
    val requestsLiveData: LiveData<List<UserMovieRequestEntity>>

    fun getRequests()

    fun getLoggedID(): Int
    fun deleteRequest(requestEntity: UserMovieRequestEntity)
    fun insetRequest(requestEntity: UserMovieRequestEntity)
}