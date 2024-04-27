package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

@Dao
interface FavouriteMoviesDao {


    @Query("SELECT * FROM favourite_movies")
    fun getFavourites(): List<FavouriteMovieEntity>

    @Insert
    fun insertMovie(favouriteMovieEntity: FavouriteMovieEntity)

    @Delete
    fun deleteMovie(favouriteMovieEntity: FavouriteMovieEntity)

    @Query("DELETE FROM favourite_movies where movieID = :id")
    fun removeMovieById(id: Int)

    @Query("SELECT isFavourite FROM favourite_movies where movieID = :movieID")
    fun checkToFavourite(movieID: Int) : Boolean

}