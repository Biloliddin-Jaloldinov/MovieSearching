package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.presentation.ui.UserReviewsScreen
import uz.jbzprojects.moviesearching.presentation.viewmodel.FavouritesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.UserReviewsViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class UserReviewsViewModelImpl : UserReviewsViewModel, ViewModel() {

    override val reviewsLiveData: LiveData<List<ReviewEntity>> get() = _reviewsLiveData

    private val _reviewsLiveData = MutableLiveData<List<ReviewEntity>>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getUserReviews() {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        _reviewsLiveData.value = repository.getUserReviews()
    }
}