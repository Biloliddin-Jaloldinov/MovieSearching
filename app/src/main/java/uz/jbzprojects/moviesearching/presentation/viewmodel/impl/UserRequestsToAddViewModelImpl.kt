package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.presentation.viewmodel.UserRequestToAddMovieViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class UserRequestsToAddViewModelImpl : UserRequestToAddMovieViewModel, ViewModel() {


    override val requestsLiveData: LiveData<List<UserMovieRequestEntity>> get() = _requestsLiveData

    private val _requestsLiveData = MutableLiveData<List<UserMovieRequestEntity>>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getRequests() {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        _requestsLiveData.value = repository.getUserRequestsToAddMovie()
    }

    override fun getLoggedID(): Int {
        return repository.getLoggedInUserID()
    }

    override fun deleteRequest(requestEntity: UserMovieRequestEntity) {
        repository.deleteUserRequestToAdd(requestEntity)
        getRequests()
    }

    override fun insetRequest(requestEntity: UserMovieRequestEntity) {
        repository.insertRequestToAddMovie(requestEntity)
        getRequests()
    }
}