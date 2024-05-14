package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity

@Dao
interface WatchLaterDao {
    @Query("SELECT * FROM watch_later_movies WHERE userID = :userID  ORDER BY id DESC")
    fun getWatchLater(userID : Int): List<WatchLaterEntity>

    @Insert
    fun insertMovie(favouriteMovieEntity: WatchLaterEntity)

    @Delete
    fun deleteMovie(favouriteMovieEntity: WatchLaterEntity)

    @Query("DELETE FROM watch_later_movies WHERE movieID = :id")
    fun removeMovieByIdFromWatchLater(id: Int)

    @Query("SELECT isWatchLater FROM watch_later_movies WHERE movieID = :movieID AND userID = :userID")
    fun checkToWatchLater(movieID: Int, userID : Int) : Boolean

}