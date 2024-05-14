package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity

interface UserReviewsViewModel {
    val error: LiveData<String>
    val reviewsLiveData: LiveData<List<ReviewEntity>>

    fun getUserReviews()
}