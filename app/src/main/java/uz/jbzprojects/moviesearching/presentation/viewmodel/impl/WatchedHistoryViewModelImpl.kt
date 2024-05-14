package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.presentation.viewmodel.FavouritesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.WatchedHistoryViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class WatchedHistoryViewModelImpl : WatchedHistoryViewModel, ViewModel() {


    override val watchedHistoryLiveData: LiveData<List<WatchedHistoryEntity>> get() = _watchedHistoryLiveData

    private val _watchedHistoryLiveData = MutableLiveData<List<WatchedHistoryEntity>>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getHistory() {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        _watchedHistoryLiveData.value = repository.getWatchedHistory()
    }

    override fun clearWatchedHistory() {
        repository.clearAllWatchedHistory()
        getHistory()
    }

}