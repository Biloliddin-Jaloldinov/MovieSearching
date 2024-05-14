package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenSettingsBinding
import uz.jbzprojects.moviesearching.presentation.viewmodel.SettingsViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.SettingsViewModelImpl

class SettingsScreen : Fragment() {

    private lateinit var binding: ScreenSettingsBinding
    private val viewModel: SettingsViewModel by viewModels<SettingsViewModelImpl>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        initButtons()
    }

    private fun updateUI() {

        if (viewModel.isLoggedIn()) {
            val user = viewModel.getUser()
            Log.d("TTT", "user: $user")
            binding.textUserName.text = user.userName
            binding.textPhoneNumber.text = user.fullName
            binding.btnLogOut.visibility = View.VISIBLE
            binding.btnLogIn.isClickable = false
            binding.imgUser.setImageResource(user.image)
        } else {
            binding.imgUser.setImageResource(R.drawable.img_unknown_user)
            binding.textUserName.text = "Войдите в систему"
            binding.textPhoneNumber.text = "Нажмите сюда чтобы войти"
            binding.btnLogOut.visibility = View.GONE
            binding.btnLogIn.isClickable = true
        }

    }

    private fun initButtons() {
        binding.btnLogIn.setOnClickListener {
        if(!viewModel.isLoggedIn()){
                val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
                navController.navigate(SettingsScreenDirections.actionSettingsScreenToLogInScreen())
            }
        }
        binding.btnFavourites.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.navigate(SettingsScreenDirections.actionSettingsScreenToFavouritesScreen())
        }
        binding.btnWatchLater.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.navigate(SettingsScreenDirections.actionSettingsScreenToWatchLaterScreen())
        }
        binding.btnReviews.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.navigate(SettingsScreenDirections.actionSettingsScreenToUserReviewsScreen())
        }
        binding.btnWatchedHistory.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.navigate(SettingsScreenDirections.actionSettingsScreenToWatchedHistoryScreen())
        }
        binding.btnMyRequest.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.navigate(SettingsScreenDirections.actionSettingsScreenToUserRequestsToAddMovieScreen())
        }
        binding.btnLogOut.setOnClickListener {
            viewModel.setLogOut()
            updateUI()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsScreen()
    }
}