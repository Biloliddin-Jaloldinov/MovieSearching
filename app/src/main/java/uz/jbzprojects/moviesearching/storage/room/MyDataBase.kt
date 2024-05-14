package uz.jbzprojects.moviesearching.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.jbzprojects.moviesearching.storage.room.dao.FavouriteMoviesDao
import uz.jbzprojects.moviesearching.storage.room.dao.ReviewsDao
import uz.jbzprojects.moviesearching.storage.room.dao.SearchingHistoryDao
import uz.jbzprojects.moviesearching.storage.room.dao.UserInfoDao
import uz.jbzprojects.moviesearching.storage.room.dao.UserRequestToAddMovieDao
import uz.jbzprojects.moviesearching.storage.room.dao.WatchLaterDao
import uz.jbzprojects.moviesearching.storage.room.dao.WatchedHistoryDao
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity

@Database(
    entities = [FavouriteMovieEntity::class, WatchLaterEntity::class, UserInfoEntity::class,
        ReviewEntity::class, WatchedHistoryEntity::class, SearchingHistoryEntity::class, UserMovieRequestEntity::class],
    version = 1
)
abstract class MyDataBase : RoomDatabase() {

    companion object {
        private var instance: MyDataBase? = null
        fun getInstance(): MyDataBase = instance!!
        fun init(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, MyDataBase::class.java, "MyDataBase")
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }

    abstract fun getFavouritesMovieDao(): FavouriteMoviesDao
    abstract fun getWatchLaterDao(): WatchLaterDao
    abstract fun getUserInfoDao(): UserInfoDao

    abstract fun getReviewsDao(): ReviewsDao
    abstract fun getWatchedHistoryDao(): WatchedHistoryDao
    abstract fun getSearchingHistoryDao(): SearchingHistoryDao
    abstract fun getUserRequestToAddMovie(): UserRequestToAddMovieDao

}