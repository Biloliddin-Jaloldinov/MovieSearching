package uz.jbzprojects.moviesearching.presentation.viewmodel

import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity

interface LogInViewModel {

    fun getUserByUserName(userName: String): UserInfoEntity
    fun setUserLogIn(userName: String)
    fun setAdminLogIn()

    fun checkUserFromDataBase(userName: String): Boolean
}