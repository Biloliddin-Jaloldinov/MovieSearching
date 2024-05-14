package uz.jbzprojects.moviesearching.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_histories")
data class SearchingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val searchedText: String,
    var userID: Int
)
