package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.presentation.viewmodel.FavouritesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.WatchLaterViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class WatchLaterViewModelImpl : WatchLaterViewModel, ViewModel() {


    override val moviesLiveData: LiveData<List<WatchLaterEntity>> get() = _moviesOffers

    private val _moviesOffers = MutableLiveData<List<WatchLaterEntity>>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getWatchLaterMovies() {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        _moviesOffers.value = repository.getWatchLaterMovies()
    }
}