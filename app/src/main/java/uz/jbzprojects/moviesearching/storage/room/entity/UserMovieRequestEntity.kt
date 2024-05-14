package uz.jbzprojects.moviesearching.storage.room.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request_movies")
data class UserMovieRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    val imageUrl: String,
    val title: String,
    val date: String,
    val description: String,
    var userID: Int
)
