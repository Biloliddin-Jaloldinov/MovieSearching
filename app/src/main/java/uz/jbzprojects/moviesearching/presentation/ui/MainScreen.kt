package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenMainBinding
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.SharedViewModel

class MainScreen : Fragment() {

    private lateinit var binding: ScreenMainBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("TTT", "onCreateView(MainScreen")
        binding = ScreenMainBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
        Log.d("TTT", "onCreateView(MainScreen")

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homePage -> {
                    navController.navigate(R.id.homeFragment)
                }

                R.id.favouritesPage -> {
                    navController.navigate(R.id.favouritesFragment)
                }

                R.id.searchPage -> {
                    navController.navigate(R.id.searchFragment)
                }
            }
            true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainScreen()
    }
}