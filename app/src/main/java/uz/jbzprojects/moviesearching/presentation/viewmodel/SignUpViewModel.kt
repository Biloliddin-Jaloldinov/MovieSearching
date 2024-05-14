package uz.jbzprojects.moviesearching.presentation.viewmodel

import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity

interface SignUpViewModel {

    fun getUserByUserName(userName: String): UserInfoEntity
    fun addNewUser(user: UserInfoEntity)

    fun checkUserFromDataBase(userName: String): Boolean
}