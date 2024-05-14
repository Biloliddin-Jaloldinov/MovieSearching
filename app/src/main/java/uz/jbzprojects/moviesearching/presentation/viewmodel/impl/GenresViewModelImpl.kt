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
import uz.jbzprojects.moviesearching.network.data.genresData.GenreData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.presentation.viewmodel.GenresViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.HomeViewModel
import uz.jbzprojects.moviesearching.utils.isConnected

class GenresViewModelImpl : GenresViewModel, ViewModel() {
    override val moviesLiveData: LiveData<List<GenreData>> get() = _moviesOffers

    private val _moviesOffers = MutableLiveData<List<GenreData>>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getMoviesGenres() {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        viewModelScope.launch {
            repository.getMovieGenres().collect { movies ->
                _moviesOffers.value = movies
            }
        }
    }
}