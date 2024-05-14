package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity

@Dao
interface UserInfoDao {
    @Insert
    fun insertUser(userEntity: UserInfoEntity)

    @Update
    fun updateUserInfo(userEntity: UserInfoEntity)

    @Delete
    fun deleteUser(userEntity: UserInfoEntity)

    @Query("SELECT * FROM user_info WHERE userID = :userID")
    fun getUserByID(userID: Int): UserInfoEntity

    @Query("SELECT * FROM user_info WHERE userName = :userName")
    fun getUserByUserName(userName: String): UserInfoEntity

    @Query("SELECT * FROM user_info WHERE userName = :userName")
    fun getUserID(userName: String): UserInfoEntity

    @Query("SELECT COUNT(*) FROM user_info WHERE userName = :userName ")
    fun getUserCount(userName: String): Int

}