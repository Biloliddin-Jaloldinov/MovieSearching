package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenLoginBinding
import uz.jbzprojects.moviesearching.presentation.viewmodel.LogInViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.LogInViewModelImpl
import java.util.Locale

class LogInScreen : Fragment() {
    private lateinit var binding: ScreenLoginBinding
    private val viewModel: LogInViewModel by viewModels<LogInViewModelImpl>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        val navHost = parentFragment as NavHostFragment
        val parent = navHost.parentFragment as MainScreen
        parent.bottomNavigation.visibility = View.GONE
    }


    private fun initButtons() {
        binding.btnLogin.setOnClickListener {
            if (binding.inputUserName.text.toString().lowercase(Locale.getDefault()) == "admin" && binding.inputPassword.text.toString() == "1234") {
                viewModel.setAdminLogIn()
                val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
                navController.popBackStack()
            } else if (isUserNameInputCorrect() && isPasswordCorrect()) {
                viewModel.setUserLogIn(binding.inputUserName.text.toString())
                val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
                navController.popBackStack()
            }
        }
        binding.btnToGetPassword.setOnClickListener {
            if (isUserNameInputCorrect()) {
                Toast.makeText(
                    requireContext(), "Пароль: " + viewModel.getUserByUserName(binding.inputUserName.text.toString()).password,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.inputPassword.addTextChangedListener {
            binding.textErrorForPassword.visibility = View.GONE
        }
        binding.inputUserName.addTextChangedListener {
            binding.textErrorForUserNama.visibility = View.GONE
        }
        binding.btnToSignUp.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.navigate(LogInScreenDirections.actionLogInScreenToSignUpScreen())
        }
    }

    private fun isUserNameInputCorrect(): Boolean {
        if (binding.inputUserName.text.length < 3) {
            binding.textErrorForUserNama.text = "Логин должен сосотоять как минимум из 3 символов"
            binding.textErrorForUserNama.visibility = View.VISIBLE
            return false
        } else if (!binding.inputUserName.text.toString().matches(Regex("[a-zA-Z0-9]+"))) {
            binding.textErrorForUserNama.text = "Логин дожен содержать только буквы и цыфры"
            binding.textErrorForUserNama.visibility = View.VISIBLE
            return false
        } else if (!viewModel.checkUserFromDataBase(binding.inputUserName.text.toString())) {
            binding.textErrorForUserNama.text = "Пользователь с таким логином не найден"
            binding.textErrorForUserNama.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun isPasswordCorrect(): Boolean {
        val user = viewModel.getUserByUserName(binding.inputUserName.text.toString())
        if (user.password != binding.inputPassword.text.toString()) {
            binding.textErrorForPassword.text = "Неправильный пароль"
            binding.textErrorForPassword.visibility = View.VISIBLE
            return false
        } else if (binding.inputPassword.text.length < 4) {
            binding.textErrorForPassword.text = "Пароль должен сосотоять как минимум из 4 цифр"
            binding.textErrorForPassword.visibility = View.VISIBLE
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        val navHost = parentFragment as NavHostFragment
        val parent = navHost.parentFragment as MainScreen
        parent.bottomNavigation.visibility = View.VISIBLE

    }

    companion object {
        @JvmStatic
        fun newInstance() = LogInScreen()
    }
}
