package uz.jbzprojects.moviesearching.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import retrofit2.http.GET
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity

@Dao
interface ReviewsDao {
    @Insert
    fun insertReview(reviewEntity: ReviewEntity)

    @Update
    fun updateReview(reviewEntity: ReviewEntity)

    @Delete
    fun deleteReview(reviewEntity: ReviewEntity)

    @Query("SELECT * FROM review_info")
    fun getAllReviews(): List<ReviewEntity>

    @Query("SELECT * FROM review_info WHERE movieID = :movieID ORDER BY id DESC")
    fun getAllReviewsByMovieID(movieID: Int): List<ReviewEntity>

    @Query("SELECT * FROM review_info WHERE userID = :userID AND movieID = :movieID")
    fun getReviewByMovieIDAndUserName(movieID: Int, userID: Int): ReviewEntity

    @Query("SELECT * FROM review_info WHERE userID = :userID")
    fun getAllUserReviews(userID: Int): List<ReviewEntity>

    @Query("SELECT * FROM review_info WHERE userID != :userID AND movieID = :movieID ORDER BY id DESC")
    fun getReviewsWithoutUser(movieID: Int, userID: Int): List<ReviewEntity>

}