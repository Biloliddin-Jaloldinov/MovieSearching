package uz.jbzprojects.moviesearching.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.jbzprojects.moviesearching.R

@Entity(tableName = "review_info")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userName: String,
    val userID: Int,
    val date: String,
    val imageUser: Int = R.drawable.img_person1,
    var rating: String,
    var comment: String,
    val movieID: Int,
    val movieTitle: String,
    val movieImage: String
)
