package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenMainBinding
import uz.jbzprojects.moviesearching.presentation.dialogs.SearchingParamsDialog

class MainScreen : Fragment() {

    private lateinit var binding: ScreenMainBinding
    private lateinit var navController: NavController
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost = childFragmentManager.findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment
        navController = navHost.navController
        bottomNavigation = binding.bottomNavigation
        bottomNavigation.setupWithNavController(navController)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainScreen()
    }
}