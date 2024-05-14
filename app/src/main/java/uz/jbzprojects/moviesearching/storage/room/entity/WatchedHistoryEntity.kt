package uz.jbzprojects.moviesearching.storage.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_histories")
data class WatchedHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "movieID")
    val movieId: Int,
    val watchedDate: String,
    val image: String,
    val title: String,
    var userID: Int
)
