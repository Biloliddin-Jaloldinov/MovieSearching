package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity

@Dao
interface WatchedHistoryDao {
    @Query("SELECT * FROM watched_histories WHERE userID = :userID  ORDER BY id DESC")
    fun getWatchHistory(userID: Int): List<WatchedHistoryEntity>

    @Query("DELETE FROM watched_histories WHERE userID = :userID AND movieID = :movieID ")
    fun deleteWatchHistoryByMovieAndUserID(userID: Int, movieID : Int)

    @Insert
    fun insertHistory(movie: WatchedHistoryEntity)

    @Delete
    fun deleteHistory(movie: WatchedHistoryEntity)

    @Query("DELETE FROM watched_histories WHERE userID = :userID")
    fun clearAllHistory(userID: Int)

    @Query("DELETE FROM watched_histories WHERE id = :historyID")
    fun deleteHistoryByID(historyID: Int)


    @Query("SELECT COUNT(*) FROM watched_histories WHERE userID = :userID")
    fun getHistoryCount(userID: Int) : Int

    @Query("DELETE FROM watched_histories WHERE id IN (SELECT id FROM watched_histories WHERE userID = :userID ORDER BY id ASC LIMIT 1)")
    fun deleteOldestHistory(userID : Int)


}