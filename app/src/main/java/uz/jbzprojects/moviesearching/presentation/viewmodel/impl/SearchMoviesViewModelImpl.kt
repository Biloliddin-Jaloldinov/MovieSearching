package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedMovies
import uz.jbzprojects.moviesearching.presentation.viewmodel.SearchMoviesViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class SearchMoviesViewModelImpl : SearchMoviesViewModel, ViewModel() {


    override val moviesLiveData: LiveData<List<SearchedMovies>> get() = _moviesOffers
    override val historiesLiveData: LiveData<List<SearchingHistoryEntity>> get() = _historiesLiveData

    private val _moviesOffers = MutableLiveData<List<SearchedMovies>>()
    private val _historiesLiveData = MutableLiveData<List<SearchingHistoryEntity>>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getSearchedMovies(query: String) {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        viewModelScope.launch {
            repository.getSearchedMovie(query).collect { movieData ->
                if (movieData?.results == null) {
                    _error.value = "Data not founded"
                } else {
                    _moviesOffers.value = movieData.results
                }
            }
        }
    }

    override fun saveSearchParams(year: String, language: String) {
        repository.saveSearchParams(year, language)
    }

    override fun getParamForSearch(): Pair<String, String> {
        return repository.getParamForSearch()
    }

    override fun getSearchingHistory() {
        _historiesLiveData.value = repository.getUserSearchingHistory()
    }

    override fun addHistory(historyEntity: SearchingHistoryEntity) {
        repository.addUserSearchingHistory(historyEntity)
        getSearchingHistory()

    }

    override fun deleteSearchingHistory(historyID: Int) {
        repository.deleteSearchingHistoryById(historyID)
        getSearchingHistory()
    }

    override fun getLoggedUserID(): Int {
        return repository.getLoggedInUserID()
    }

}