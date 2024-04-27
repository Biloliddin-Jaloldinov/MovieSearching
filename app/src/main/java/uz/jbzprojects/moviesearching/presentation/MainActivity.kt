package uz.jbzprojects.moviesearching.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.presentation.ui.FavouritesScreen
import uz.jbzprojects.moviesearching.presentation.ui.HomeScreen
import uz.jbzprojects.moviesearching.presentation.ui.MainScreen
import uz.jbzprojects.moviesearching.presentation.ui.SearchScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.activityAttachToFragment, MainScreen.newInstance()).commit()
    }

}