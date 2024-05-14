package uz.jbzprojects.moviesearching.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.app.App
import uz.jbzprojects.moviesearching.network.ApiClient
import uz.jbzprojects.moviesearching.network.data.genresData.GenreData
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedResponse
import uz.jbzprojects.moviesearching.storage.room.MyDataBase
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity
import uz.jbzprojects.moviesearching.storage.sharedpreferences.AppPreferences

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

    private val pref = AppPreferences.getInstance()
    private val favouriteMoviesDao = MyDataBase.getInstance().getFavouritesMovieDao()
    private val watchLaterDao = MyDataBase.getInstance().getWatchLaterDao()
    private val userInfoDao = MyDataBase.getInstance().getUserInfoDao()
    private val reviewDao = MyDataBase.getInstance().getReviewsDao()
    private val watchedHistoryDao = MyDataBase.getInstance().getWatchedHistoryDao()
    private val searchingHistoryDao = MyDataBase.getInstance().getSearchingHistoryDao()
    private val userRequestToAdd = MyDataBase.getInstance().getUserRequestToAddMovie()

    private val mainApi = ApiClient.mainApi()

    init {
        setAdmins()
    }

    private fun setAdmins() {
        if (getLoggedInUserID() == -1)
            userInfoDao.insertUser(
                UserInfoEntity(
                    userID = 0,
                    userName = "admin",
                    fullName = "Можете модерировать комментарии",
                    password = "1234",
                    image = R.drawable.img_admin,
                    phoneNumber = "Нет номера"
                )
            )
    }


    suspend fun getAllMovies(): Flow<List<MovieData>> {
        return flow {
            val response = mainApi.discoverMovies(
                apiKey = "Bearer ${App.context.resources.getString(R.string.api_access_token)}",
                language = pref.getSearchingLanguage(),
                genre = ""
            )
            emit(response.movies)
        }.catch { e ->
            e.printStackTrace()
            emit(emptyList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMoviesByGenre(genre: String): Flow<List<MovieData>> {
        return flow {
            val response = mainApi.discoverMovies(
                apiKey = "Bearer ${App.context.resources.getString(R.string.api_access_token)}",
                language = pref.getSearchingLanguage(),
                genre = genre
            )
            emit(response.movies)
        }.catch { e ->
            e.printStackTrace()
            emit(emptyList())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMovieGenres(): Flow<List<GenreData>> {
        return flow {
            val response = mainApi.getMovieGenres(
                apiKey = "Bearer ${App.context.resources.getString(R.string.api_access_token)}",
                language = pref.getSearchingLanguage(),
            )
            emit(response.genres)
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
                language = pref.getSearchingLanguage()
            )
            emit(response)
        }.catch { e ->
            e.printStackTrace()
            emit(null)
        }.flowOn(Dispatchers.IO)
    }

    fun saveSearchParams(year: String, language: String) {
        pref.setMovieRealisedYear(year)
        pref.setSearchingLanguage(language)
    }

    fun getParamForSearch(): Pair<String, String> {
        return Pair(pref.getMovieRealisedYear(), pref.getSearchingLanguage())
    }

    suspend fun getSearchedMovie(query: String): Flow<SearchedResponse?> {
        return flow {
            val response = mainApi.getSearchedMovies(
                apiKey = "Bearer ${App.context.resources.getString(R.string.api_access_token)}",
                query = query,
                language = pref.getSearchingLanguage(),
                includeAdult = false,
                year = pref.getMovieRealisedYear(),
                page = 1
            )
            emit(response)
        }.catch { e ->
            e.printStackTrace()
            emit(null)
        }.flowOn(Dispatchers.IO)
    }

    fun addMovieToFavourite(movieData: FavouriteMovieEntity) {
        movieData.userID = getLoggedInUserID()
        favouriteMoviesDao.insertMovie(movieData)
    }

    fun removeMovieFromFavourites(movieID: Int) {
        favouriteMoviesDao.removeMovieById(movieID)
    }

    fun getFavouriteMovies(): List<FavouriteMovieEntity> {
        return favouriteMoviesDao.getFavourites(getLoggedInUserID())
    }

    fun checkToFavourite(movieID: Int): Boolean {
        return favouriteMoviesDao.checkToFavourite(movieID, getLoggedInUserID())
    }

    fun addMovieToWatchLater(movieData: WatchLaterEntity) {
        movieData.userID = getLoggedInUserID()
        watchLaterDao.insertMovie(movieData)
    }

    fun removeMovieFromWatchLater(movieID: Int) {
        watchLaterDao.removeMovieByIdFromWatchLater(movieID)
    }

    fun getWatchLaterMovies(): List<WatchLaterEntity> {
        return watchLaterDao.getWatchLater(getLoggedInUserID())
    }

    fun checkToWatchLater(movieID: Int): Boolean {
        return watchLaterDao.checkToWatchLater(movieID, getLoggedInUserID())
    }

    fun addNewUser(userInfo: UserInfoEntity) {
        userInfoDao.insertUser(userInfo)
        setLogInUser(userInfo.userName)
    }

    fun setLogInUser(userName: String) {
        pref.setLogin(userInfoDao.getUserID(userName).userID)
    }

    fun setAdminLogIn() {
        pref.setLogin(1)
    }

    fun getUserByUserName(userName: String): UserInfoEntity {
        return userInfoDao.getUserByUserName(userName)
    }

    fun getUserById(): UserInfoEntity {
        return userInfoDao.getUserByID(getLoggedInUserID())
    }

    fun setUserLogOut() {
        pref.setLogin(-1)
    }

    fun checkToLogin(): Boolean {
        return pref.getLogin() != -1
    }

    fun checkUserFromDB(userName: String): Boolean {
        return userInfoDao.getUserCount(userName) > 0
    }

    fun getLoggedInUserID(): Int {
        Log.d("TTT", "Check id login: " + pref.getLogin())
        return pref.getLogin()
    }

    fun getReviewsByMovieID(movieID: Int): List<ReviewEntity> {
        val reviews = ArrayList<ReviewEntity>()
        reviews.addAll(
            reviewDao.getReviewsWithoutUser(movieID, getLoggedInUserID())
        )
        return reviews
    }

    fun getAllReviews(): List<ReviewEntity> {
        return reviewDao.getAllReviews()
    }

    fun getUserReviews(): List<ReviewEntity> {
        return reviewDao.getAllUserReviews(getLoggedInUserID())
    }

    fun addUserReview(review: ReviewEntity) {
        reviewDao.insertReview(review)
    }

    fun deleteReview(review: ReviewEntity) {
        reviewDao.deleteReview(review)
    }

    fun updateReview(review: ReviewEntity) {
        reviewDao.updateReview(review)
    }

    fun getUserReview(movieID: Int): ReviewEntity {
        return reviewDao.getReviewByMovieIDAndUserName(movieID, getLoggedInUserID())
    }

    fun getWatchedHistory(): List<WatchedHistoryEntity> {
        return watchedHistoryDao.getWatchHistory(getLoggedInUserID())
    }

    fun clearAllWatchedHistory() {
        watchedHistoryDao.clearAllHistory(getLoggedInUserID())
    }

    fun insertWatchedHistory(watchedHistoryEntity: WatchedHistoryEntity) {
        if (watchedHistoryDao.getHistoryCount(getLoggedInUserID()) == 15) {
            watchedHistoryDao.deleteOldestHistory(getLoggedInUserID())
        }
        watchedHistoryDao.deleteWatchHistoryByMovieAndUserID(watchedHistoryEntity.userID, watchedHistoryEntity.movieId)
        watchedHistoryDao.insertHistory(watchedHistoryEntity)
    }

    fun getUserSearchingHistory(): List<SearchingHistoryEntity> {
        return searchingHistoryDao.getUserSearchingHistory(getLoggedInUserID())
    }

    fun addUserSearchingHistory(searchingHistory: SearchingHistoryEntity) {
        if (searchingHistoryDao.getHistoryCount(getLoggedInUserID()) == 7) {
            searchingHistoryDao.deleteOldestSearchedHistory(getLoggedInUserID())
        }
        searchingHistoryDao.deleteSearchingHistoryByMovieAndUserID(searchingHistory.userID, searchingHistory.searchedText)
        searchingHistoryDao.addHistory(searchingHistory)
    }

    fun deleteSearchingHistoryById(historyID: Int) {
        searchingHistoryDao.deleteHistoryByID(historyID)
    }

    fun insertRequestToAddMovie(userMovieRequestEntity: UserMovieRequestEntity) {
        userRequestToAdd.insertRequest(userMovieRequestEntity)
    }

    fun deleteUserRequestToAdd(userMovieRequestEntity: UserMovieRequestEntity) {
        userRequestToAdd.deleteRequest(userMovieRequestEntity)
    }

    fun getUserRequestsToAddMovie(): List<UserMovieRequestEntity> {
        return userRequestToAdd.getUserRequests(getLoggedInUserID())
    }
}

