package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity

interface WatchedHistoryViewModel {
    val error: LiveData<String>
    val watchedHistoryLiveData: LiveData<List<WatchedHistoryEntity>>

    fun getHistory()
    fun clearWatchedHistory()
}