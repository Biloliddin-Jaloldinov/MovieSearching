package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity

interface MovieInfoViewModel {
    val error: LiveData<String>
    val movieLiveData: LiveData<OneMovieData>
    val reviewsLiveData: LiveData<List<ReviewEntity>>
    val userReviewLiveData: LiveData<ReviewEntity?>

    fun getMovieByID(movieID: Int)
    fun getLoggedID() : Int

    fun addMovieToFavourite(movieData: FavouriteMovieEntity)
    fun removeMovieFromWatchLater(movieID: Int)
    fun checkMovieToWatchLater(movieID: Int): Boolean
    fun addMovieToWatchLater(movieData: WatchLaterEntity)
    fun removeMovieFromFavourites(movieID: Int)
    fun checkMovieToFavourite(movieID: Int): Boolean
    fun checkToLoggedIn(): Boolean

    fun addUserReview(review: ReviewEntity)

    fun updateUserReview(review: ReviewEntity)
    fun deleteUserReview(review: ReviewEntity)
    fun deleteReview(review: ReviewEntity)

    fun getUserReview(movieID: Int)

    fun getUserInfo() : UserInfoEntity

    fun addToWatchedHistory(watchedHistoryEntity: WatchedHistoryEntity)

}