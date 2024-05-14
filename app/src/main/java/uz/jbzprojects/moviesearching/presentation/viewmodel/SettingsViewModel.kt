package uz.jbzprojects.moviesearching.presentation.viewmodel

import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity

interface SettingsViewModel {
    fun getUser() : UserInfoEntity
    fun isLoggedIn(): Boolean
    fun setLogOut()
}