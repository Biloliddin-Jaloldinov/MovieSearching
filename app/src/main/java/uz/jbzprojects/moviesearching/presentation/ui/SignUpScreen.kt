package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenSignupBinding
import uz.jbzprojects.moviesearching.presentation.viewmodel.SignUpViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.SignUpViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.UserInfoEntity

class SignUpScreen : Fragment() {
    private lateinit var binding: ScreenSignupBinding
    private val viewModel: SignUpViewModel by viewModels<SignUpViewModelImpl>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }


    private fun initButtons() {
        binding.inputPassword.addTextChangedListener {
            binding.textErrorForPassword.visibility = View.GONE
        }
        binding.inputUserName.addTextChangedListener {
            binding.textErrorForUserNama.visibility = View.GONE
        }

        binding.btnSignUp.setOnClickListener {
            if (isInputCorrect()) {
                viewModel.addNewUser(
                    UserInfoEntity(
                        userName = binding.inputUserName.text.toString(),
                        password = binding.inputPassword.text.toString()
                    )
                )
                val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
                navController.popBackStack(R.id.homeScreen, false)
            }
        }
        binding.btnToLogIn.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.popBackStack()
        }
    }

    private fun isInputCorrect(): Boolean {
        return if (binding.inputUserName.text.length < 3) {
            binding.textErrorForUserNama.text = "Логин должен сосотоять как минимум из 3 символов"
            binding.textErrorForUserNama.visibility = View.VISIBLE
            false
        } else if (!binding.inputUserName.text.toString().matches(Regex("[a-zA-Z0-9]+"))) {
            binding.textErrorForUserNama.text = "Логин дожен содержать только буквы и цыфры"
            binding.textErrorForUserNama.visibility = View.VISIBLE
            false

        } else if (binding.inputUserName.text.toString().toLowerCase() == "admin" ||
            viewModel.checkUserFromDataBase(binding.inputUserName.text.toString())
        ) {
            binding.textErrorForUserNama.text = "Пользователь с таким логином уже существует"
            binding.textErrorForUserNama.visibility = View.VISIBLE
            false
        } else if (binding.inputPassword.text.length < 4) {
            binding.textErrorForPassword.visibility = View.VISIBLE
            false
        } else true
    }


    override fun onResume() {
        super.onResume()
        val navHost = parentFragment as NavHostFragment
        val parent = navHost.parentFragment as MainScreen
        parent.bottomNavigation.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        val navHost = parentFragment as NavHostFragment
        val parent = navHost.parentFragment as MainScreen
        parent.bottomNavigation.visibility = View.VISIBLE

    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpScreen()
    }
}
