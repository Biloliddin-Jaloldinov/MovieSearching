package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenMyRequestToAddMovieBinding
import uz.jbzprojects.moviesearching.presentation.adapter.UserRequestToAddMovieAdapter
import uz.jbzprojects.moviesearching.presentation.dialogs.AddUserRequestToAddMovieDialog
import uz.jbzprojects.moviesearching.presentation.viewmodel.UserRequestToAddMovieViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.UserRequestsToAddViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity

class UserRequestsToAddMovieScreen : Fragment() {
    private lateinit var binding: ScreenMyRequestToAddMovieBinding
    private val viewModel: UserRequestToAddMovieViewModel by viewModels<UserRequestsToAddViewModelImpl>()
    private lateinit var adapter: UserRequestToAddMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenMyRequestToAddMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.requestsLiveData.observe(viewLifecycleOwner, requestsObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun initButtons() {
        adapter = UserRequestToAddMovieAdapter()
        binding.rvMyRequests.adapter = adapter

        binding.btnAddMovie.setOnClickListener {
            val loggedID = viewModel.getLoggedID()
            if (loggedID >= 0) {
                val addUserRequestDialog = AddUserRequestToAddMovieDialog(loggedID)
                addUserRequestDialog.setSaveClickListener { request ->
                    viewModel.insetRequest(request)
                    addUserRequestDialog.dismiss()
                }
                addUserRequestDialog.show(childFragmentManager, "addReviewDialog")
            } else {
                Toast.makeText(requireContext(), "Войдити чтобы оставить запрос", Toast.LENGTH_SHORT).show()
            }

        }
        adapter.setItemLongClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Вы хотите удалить \"${it.title}\" из запросов?")
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.deleteRequest(it)
            }
            builder.setNegativeButton("Отмена") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }
        binding.btnBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.popBackStack()
        }

    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val requestsObserver = Observer<List<UserMovieRequestEntity>> { movies ->
        updateUi(movies)
    }


    private fun updateUi(movies: List<UserMovieRequestEntity>) {
        adapter.submitList(movies)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getRequests()
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
        fun newInstance() = UserRequestsToAddMovieScreen()
    }
}
