package uz.jbzprojects.moviesearching.presentation.viewmodel

import androidx.lifecycle.LiveData
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedMovies

interface SearchMoviesViewModel {
    val error: LiveData<String>
    val moviesLiveData: LiveData<List<SearchedMovies>>
    fun getSearchedMovies(query: String)
}