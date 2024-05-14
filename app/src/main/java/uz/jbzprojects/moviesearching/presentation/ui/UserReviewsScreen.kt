package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenUserReviewsBinding
import uz.jbzprojects.moviesearching.presentation.adapter.ReviewsAdapter
import uz.jbzprojects.moviesearching.presentation.adapter.UserReviewsAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.UserReviewsViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.WatchLaterViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.UserReviewsViewModelImpl
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.WatchLaterViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity

class UserReviewsScreen : Fragment() {
    private lateinit var binding: ScreenUserReviewsBinding
    private val viewModel: UserReviewsViewModel by viewModels<UserReviewsViewModelImpl>()
    private lateinit var adapter: UserReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenUserReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.reviewsLiveData.observe(viewLifecycleOwner, watchLaterObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun initButtons() {
        adapter = UserReviewsAdapter()
        binding.rvReviews.adapter = adapter

        adapter.setItemClickListener { moiveID ->
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            val action = UserReviewsScreenDirections.actionUserReviewsScreenToInfoScreen(moiveID)
            navController.navigate(action)
        }

        binding.btnBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.popBackStack()
        }

    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val watchLaterObserver = Observer<List<ReviewEntity>> { movies ->
        updateUi(movies)
    }


    private fun updateUi(movies: List<ReviewEntity>) {
        adapter.submitList(movies)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getUserReviews()
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
        fun newInstance() = UserReviewsScreen()
    }
}
