package uz.jbzprojects.moviesearching.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.app.App
import uz.jbzprojects.moviesearching.network.ApiClient
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedResponse
import uz.jbzprojects.moviesearching.storage.room.MyDataBase
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

class Repository private constructor() {

    companion object {
        private var repository: Repository? = null
        fun getInstance(): Repository {
            if (repository == null) {
                repository = Repository()
            }
            return repository!!
        }

    }

    private val myDataBase = MyDataBase.getInstance().getFavouritesMovieDao()
    private val mainApi = ApiClient.mainApi()

    suspend fun getAllMovies(): Flow<List<MovieData>> {
        return flow {
            val response = mainApi.discoverMovies(
                apiKey = "Bearer ${App.context.resources.getString(R.string.api_access_token)}",
                language = "ru-RU"
            )
            emit(response.movies)
        }.catch { e ->
            e.printStackTrace()
            emit(emptyList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMovieByID(movieID: Int): Flow<OneMovieData?> {
        return flow {
            val response = mainApi.getMovieByID(
                apiKey = "Bearer ${App.context.resources.getString(R.string.api_access_token)}",
                movieID = movieID,
                language = "ru-RU"
            )
            emit(response)
        }.catch { e ->
            e.printStackTrace()
            emit(null)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchedMovie(query: String): Flow<SearchedResponse?> {
        return flow {
            val response = mainApi.getSearchedMovies(
                apiKey = "Bearer ${App.context.resources.getString(R.string.api_access_token)}",
                query = query,
                language = "ru-RU",
                includeAdult = false,
                page = 1
            )
            emit(response)
        }.catch { e ->
            e.printStackTrace()
            emit(null)
        }.flowOn(Dispatchers.IO)
    }

    fun addMovieToFavourite(movieData: FavouriteMovieEntity) {
        myDataBase.insertMovie(movieData)
    }

    fun removeMovieFromFavourites(movieID: Int) {
        myDataBase.removeMovieById(movieID)
    }

    fun getFavouriteMovies(): List<FavouriteMovieEntity> {
        return myDataBase.getFavourites()
    }

    fun checkToFavourite(movieID: Int): Boolean {
        return myDataBase.checkToFavourite(movieID)
    }
}