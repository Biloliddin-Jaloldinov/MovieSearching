package uz.jbzprojects.moviesearching.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.jbzprojects.moviesearching.R

@Entity(tableName = "user_info", indices = [androidx.room.Index(value = ["userName"], unique = true)])
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val userID: Int = 0,
    val fullName: String = "",
    val userName: String,
    val password: String,
    val image: Int = R.drawable.img_person1,
    val phoneNumber: String = ""
)
