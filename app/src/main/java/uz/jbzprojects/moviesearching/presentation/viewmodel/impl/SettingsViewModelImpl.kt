package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.jbzprojects.moviesearching.domain.Repository
import uz.jbzprojects.moviesearching.presentation.viewmodel.FavouritesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.SettingsViewModel
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity
import uz.jbzprojects.moviesearching.utils.isConnected

class SettingsViewModelImpl : SettingsViewModel, ViewModel() {

    private val repository = Repository.getInstance()
    override fun getUser(): UserInfoEntity {
        return repository.getUserById()
    }

    override fun isLoggedIn(): Boolean {
        return repository.checkToLogin()
    }

    override fun setLogOut() {
        repository.setUserLogOut()
    }


}