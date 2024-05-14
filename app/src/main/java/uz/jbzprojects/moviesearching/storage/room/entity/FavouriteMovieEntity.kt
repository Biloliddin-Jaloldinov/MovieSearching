package uz.jbzprojects.moviesearching.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class FavouriteMovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "movieID")
    val movieId: Int,
    val isFavourite: Boolean,
    val image: String,
    val title: String,
    val date: String,
    var userID: Int
)
