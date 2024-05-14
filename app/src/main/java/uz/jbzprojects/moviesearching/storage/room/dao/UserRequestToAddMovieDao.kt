package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity

@Dao
interface UserRequestToAddMovieDao {
    @Query("SELECT * FROM request_movies WHERE userID = :userID  ORDER BY movieId DESC")
    fun getUserRequests(userID : Int): List<UserMovieRequestEntity>

    @Insert
    fun insertRequest(movieDate: UserMovieRequestEntity)

    @Delete
    fun deleteRequest(movieDate: UserMovieRequestEntity)

}