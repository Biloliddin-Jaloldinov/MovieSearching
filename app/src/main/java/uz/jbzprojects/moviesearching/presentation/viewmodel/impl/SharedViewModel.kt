package uz.jbzprojects.moviesearching.presentation.viewmodel.impl

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val bundleFromFragmentBToFragmentA = MutableLiveData<Bundle>()
}