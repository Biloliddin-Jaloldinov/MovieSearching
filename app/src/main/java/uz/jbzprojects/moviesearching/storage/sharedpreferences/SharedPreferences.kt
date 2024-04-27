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
    fun setLogin(login: String) {
        pref.edit().putString("LOGIN", login).apply()
    }

    fun getLogin(): String {
        return pref.getString("LOGIN", "LOGIN")!!
    }
}
