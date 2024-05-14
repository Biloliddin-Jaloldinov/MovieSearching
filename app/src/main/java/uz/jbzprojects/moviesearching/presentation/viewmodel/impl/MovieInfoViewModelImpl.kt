package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.presentation.viewmodel.MovieInfoViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class MovieInfoViewModelImpl : MovieInfoViewModel, ViewModel() {

    override val movieLiveData: LiveData<OneMovieData> get() = _moviesInfo
    override val reviewsLiveData: LiveData<List<ReviewEntity>> get() = _reviewsLiveData
    override val userReviewLiveData: LiveData<ReviewEntity?> get() = _userReviewLiveData


    private val _userReviewLiveData = MutableLiveData<ReviewEntity>()
    private val _reviewsLiveData = MutableLiveData<List<ReviewEntity>>()
    private val _moviesInfo = MutableLiveData<OneMovieData>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getMovieByID(movieID: Int) {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        viewModelScope.launch {
            repository.getMovieByID(movieID).collect { movie ->
                if (movie == null) {
                    _error.value = "Null DATA"
                } else {
                    _moviesInfo.value = movie!!

                }
            }
        }
        getMovieReviews(movieID)
    }


    override fun getLoggedID(): Int {
        return repository.getLoggedInUserID()
    }

    override fun addMovieToFavourite(movieData: FavouriteMovieEntity) {
        repository.addMovieToFavourite(movieData)
    }

    override fun removeMovieFromFavourites(movieID: Int) {
        repository.removeMovieFromFavourites(movieID)
    }

    override fun checkMovieToFavourite(movieID: Int): Boolean {
        return repository.checkToFavourite(movieID)
    }

    override fun addMovieToWatchLater(movieData: WatchLaterEntity) {
        repository.addMovieToWatchLater(movieData)
    }

    override fun removeMovieFromWatchLater(movieID: Int) {
        repository.removeMovieFromWatchLater(movieID)
    }

    override fun checkMovieToWatchLater(movieID: Int): Boolean {
        return repository.checkToWatchLater(movieID)
    }

    override fun checkToLoggedIn(): Boolean {
        return repository.checkToLogin()
    }

    override fun addUserReview(review: ReviewEntity) {
        repository.addUserReview(review)
        getUserReview(review.movieID)

    }

    override fun deleteUserReview(review: ReviewEntity) {
        repository.deleteReview(review)
        getUserReview(review.movieID)
    }

    override fun deleteReview(review: ReviewEntity) {
        repository.deleteReview(review)
        getMovieReviews(review.movieID)

    }


    override fun updateUserReview(review: ReviewEntity) {
        repository.updateReview(review)
        getUserReview(review.movieID)
    }

    override fun getUserReview(movieID: Int) {
        _userReviewLiveData.value = repository.getUserReview(movieID)
    }

    override fun getUserInfo(): UserInfoEntity {
        return repository.getUserById()
    }

    private fun getMovieReviews(movieID: Int) {
        _reviewsLiveData.value = repository.getReviewsByMovieID(movieID)
    }

    override fun addToWatchedHistory(watchedHistoryEntity: WatchedHistoryEntity) {
        repository.insertWatchedHistory(watchedHistoryEntity)
    }
}