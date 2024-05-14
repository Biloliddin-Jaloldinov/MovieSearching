package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.presentation.viewmodel.MoviesByGenreViewModel
import uz.jbzprojects.moviesearching.utils.isConnected

class MoviesByGenreViewModelImpl : MoviesByGenreViewModel, ViewModel() {
    override val moviesLiveData: LiveData<List<MovieData>> get() = _moviesOffers

    private val _moviesOffers = MutableLiveData<List<MovieData>>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getMoviesByGenre(genre: String) {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        viewModelScope.launch {
            repository.getMoviesByGenre(genre).collect { movies ->
                Log.d("TTT", movies.toString())
                _moviesOffers.value = movies
            }
        }
    }
}