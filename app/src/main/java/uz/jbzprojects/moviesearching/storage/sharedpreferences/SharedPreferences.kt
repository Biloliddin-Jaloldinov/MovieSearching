package uz.jbzprojects.moviesearching.storage.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import uz.jbzprojects.moviesearching.app.App


class AppPreferences private constructor() {
    companion object {
        private lateinit var pref: SharedPreferences
        private lateinit var instance: AppPreferences

        fun getInstance(): AppPreferences {
            pref = App.context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
            instance = AppPreferences()
            return instance
        }
    }

    fun setLogin(loginID: Int) {
        pref.edit().putInt("LOGIN", loginID).apply()
    }

    fun getLogin(): Int {
        return pref.getInt("LOGIN", -1)
    }

    fun setMovieRealisedYear(year: String) {
        pref.edit().putString("REALISED_YEAR", year).apply()
    }

    fun getMovieRealisedYear(): String {
        return pref.getString("REALISED_YEAR", "").toString()
    }

    fun setSearchingLanguage(language: String) {
        pref.edit().putString("LANGUAGE", language).apply()
    }

    fun getSearchingLanguage(): String {
        return pref.getString("LANGUAGE", "ru-RU").toString()
    }

}
