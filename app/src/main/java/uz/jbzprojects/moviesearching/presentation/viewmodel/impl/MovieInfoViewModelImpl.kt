package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.presentation.viewmodel.MovieInfoViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class MovieInfoViewModelImpl : MovieInfoViewModel, ViewModel() {

    override val movieLiveData: LiveData<OneMovieData> get() = _moviesOffers
    private val _moviesOffers = MutableLiveData<OneMovieData>()
    private val _error = MutableLiveData<String>()

    override val error: LiveData<String> get() = _error
    private val repository = Repository.getInstance()

    override fun getMovieByID(movieID : Int) {
        if (!isConnected()) {
            _error.value = "Internet not connected"
            return
        }
        viewModelScope.launch {
            repository.getMovieByID(movieID).collect { movie ->
                if (movie == null){
                    _error.value = "Null DATA"
                }else{
                    _moviesOffers.value = movie!!
                }
            }
        }
    }

    override fun addMovieToFavourite(movieData: FavouriteMovieEntity) {
        repository.addMovieToFavourite(movieData)
    }

    override fun removeMovieFromFavourites(movieID: Int) {
        repository.removeMovieFromFavourites(movieID)
    }

    override fun checkMovieToFavourite(movieID: Int): Boolean {
        return repository.checkToFavourite(movieID)
    }
}