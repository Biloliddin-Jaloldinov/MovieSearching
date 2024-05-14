package uz.jbzprojects.moviesearching.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "watch_later_movies")
data class WatchLaterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "movieID")
    val movieId: Int,
    val isWatchLater: Boolean,
    val image: String,
    val title: String,
    val date: String,
    var userID: Int
)
