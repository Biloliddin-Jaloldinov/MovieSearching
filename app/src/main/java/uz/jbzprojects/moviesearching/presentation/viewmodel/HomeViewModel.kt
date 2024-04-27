package uz.jbzprojects.moviesearching.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData

interface HomeViewModel {
    val bundleFromFragmentBToFragmentA : LiveData<Bundle>
    val error: LiveData<String>
    val moviesLiveData: LiveData<List<MovieData>>

    fun getMovies()
}