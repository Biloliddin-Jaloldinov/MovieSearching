package uz.jbzprojects.moviesearching.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import uz.jbzprojects.moviesearching.network.data.genresData.MovieGenres
import uz.jbzprojects.moviesearching.network.data.maindata.AllMovies
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedResponse

interface MainApi {

    @GET("3/discover/movie")
    suspend fun discoverMovies(
        @Header("Authorization") apiKey: String,
        @Query("language") language: String,
        @Query("with_genres") genre : String,
    ): AllMovies

    @GET("3/movie/{movie_id}")
    suspend fun getMovieByID(
        @Header("Authorization") apiKey: String,
        @Path("movie_id") movieID: Int,
        @Query("language") language: String
    ): OneMovieData?

    @GET("3/search/movie")
    suspend fun getSearchedMovies(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("primary_release_year") year: String,
        @Query("page") page: Int
    ): SearchedResponse?

    @GET("3/genre/movie/list")
    suspend fun getMovieGenres(
        @Header("Authorization") apiKey: String,
        @Query("language") language: String,
    ) : MovieGenres
}


