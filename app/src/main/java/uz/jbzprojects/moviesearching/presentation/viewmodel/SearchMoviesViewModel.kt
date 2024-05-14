package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedMovies
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity

interface SearchMoviesViewModel {
    val error: LiveData<String>
    val moviesLiveData: LiveData<List<SearchedMovies>>
    val historiesLiveData: LiveData<List<SearchingHistoryEntity>>
    fun getSearchedMovies(query: String)
    fun saveSearchParams(year: String, language: String)
    fun getParamForSearch(): Pair<String, String>

    fun getSearchingHistory()
    fun addHistory(historyEntity: SearchingHistoryEntity)
    fun deleteSearchingHistory(historyID: Int)
    fun getLoggedUserID() : Int
}