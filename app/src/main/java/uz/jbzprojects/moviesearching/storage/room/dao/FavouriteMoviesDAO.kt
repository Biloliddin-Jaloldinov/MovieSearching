package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

@Dao
interface FavouriteMoviesDao {
    @Query("SELECT * FROM favourite_movies WHERE userID = :userID  ORDER BY id DESC")
    fun getFavourites(userID : Int): List<FavouriteMovieEntity>

    @Insert
    fun insertMovie(favouriteMovieEntity: FavouriteMovieEntity)

    @Delete
    fun deleteMovie(favouriteMovieEntity: FavouriteMovieEntity)

    @Query("DELETE FROM favourite_movies WHERE movieID = :id")
    fun removeMovieById(id: Int)

    @Query("SELECT isFavourite FROM favourite_movies WHERE movieID = :movieID AND userID = :userID")
    fun checkToFavourite(movieID: Int, userID : Int) : Boolean

}