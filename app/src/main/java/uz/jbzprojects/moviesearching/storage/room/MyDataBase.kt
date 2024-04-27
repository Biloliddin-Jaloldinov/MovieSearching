package uz.jbzprojects.moviesearching.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.jbzprojects.moviesearching.storage.room.dao.FavouriteMoviesDao
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

@Database(entities = [FavouriteMovieEntity::class], version = 1)
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

}