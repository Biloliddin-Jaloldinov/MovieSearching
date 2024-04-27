package uz.jbzprojects.moviesearching.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.jbzprojects.moviesearching.app.App

object ApiClient {
    private val myClient = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor.Builder(App.context).build())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org")
        .client(myClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun mainApi(): MainApi = retrofit.create(MainApi::class.java)

}



