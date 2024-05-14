package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.presentation.viewmodel.FavouritesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.LogInViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class LogInViewModelImpl : LogInViewModel, ViewModel() {
    private val repository = Repository.getInstance()
    override fun getUserByUserName(userName: String): UserInfoEntity {
        return repository.getUserByUserName(userName)
    }

    override fun setUserLogIn(userName: String) {
        return repository.setLogInUser(userName)
    }

    override fun setAdminLogIn() {
        repository.setAdminLogIn()
    }
    override fun checkUserFromDataBase(userName: String): Boolean {
        return repository.checkUserFromDB(userName)
    }


}