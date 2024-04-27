package uz.jbzprojects.moviesearching.app

import android.app.Application
import uz.jbzprojects.moviesearching.storage.room.MyDataBase

class App : Application() {
    companion object {
        lateinit var context: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        MyDataBase.init(context)
    }
}