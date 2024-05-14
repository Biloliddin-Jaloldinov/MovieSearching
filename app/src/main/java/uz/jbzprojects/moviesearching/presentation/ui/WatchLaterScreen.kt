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
import uz.jbzprojects.moviesearching.databinding.ScreenFavouritesBinding
import uz.jbzprojects.moviesearching.databinding.ScreenWatchLaterBinding
import uz.jbzprojects.moviesearching.presentation.adapter.FavouritesAdapter
import uz.jbzprojects.moviesearching.presentation.adapter.WatchLaterAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.FavouritesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.WatchLaterViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.FavouritesViewModelImpl
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.WatchLaterViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity

class WatchLaterScreen : Fragment() {
    private lateinit var binding: ScreenWatchLaterBinding
    private val viewModel: WatchLaterViewModel by viewModels<WatchLaterViewModelImpl>()
    private lateinit var adapter: WatchLaterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenWatchLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner, watchLaterObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun initButtons() {
        adapter = WatchLaterAdapter()
        binding.rvFavourites.adapter = adapter

        adapter.setItemClickListener { moiveID ->
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            val action = WatchLaterScreenDirections.actionWatchLaterScreenToInfoScreen(moiveID)
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

    private val watchLaterObserver = Observer<List<WatchLaterEntity>> { movies ->
        updateUi(movies)
    }


    private fun updateUi(movies: List<WatchLaterEntity>) {
        adapter.submitList(movies)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getWatchLaterMovies()
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
        fun newInstance() = WatchLaterScreen()
    }
}
