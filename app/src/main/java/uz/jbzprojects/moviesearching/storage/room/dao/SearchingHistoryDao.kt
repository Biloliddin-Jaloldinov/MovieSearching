package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity

@Dao
interface SearchingHistoryDao {
    @Query("SELECT * FROM search_histories WHERE userID = :userID  ORDER BY id DESC")
    fun getUserSearchingHistory(userID: Int): List<SearchingHistoryEntity>

    @Query("DELETE FROM search_histories WHERE userID = :userID AND searchedText = :query ")
    fun deleteSearchingHistoryByMovieAndUserID(userID: Int, query: String)

    @Insert
    fun addHistory(history: SearchingHistoryEntity)

    @Delete
    fun deleteHistory(history: SearchingHistoryEntity)

    @Query("DELETE FROM search_histories WHERE userID = :userID")
    fun clearAllHistory(userID: Int)

    @Query("DELETE FROM search_histories WHERE id = :historyID")
    fun deleteHistoryByID(historyID: Int)


    @Query("SELECT COUNT(*) FROM search_histories WHERE userID = :userID")
    fun getHistoryCount(userID: Int): Int

    @Query("DELETE FROM search_histories WHERE id IN (SELECT id FROM search_histories WHERE userID = :userID ORDER BY id ASC LIMIT 1)")
    fun deleteOldestSearchedHistory(userID: Int)


}