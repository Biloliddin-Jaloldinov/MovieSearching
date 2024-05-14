package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenHomeBinding
import uz.jbzprojects.moviesearching.databinding.ScreenMoviesByGenreBinding
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.presentation.adapter.HomeAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.HomeViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.MoviesByGenreViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.HomeViewModelImpl
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.MoviesByGenreViewModelImpl

class MoviesByGenreScreen : Fragment() {
    private lateinit var genre: String
    private lateinit var binding: ScreenMoviesByGenreBinding
    private val viewModel: MoviesByGenreViewModel by lazy {
        ViewModelProvider(this).get(MoviesByGenreViewModelImpl::class.java)
    }
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = MoviesByGenreScreenArgs.fromBundle(requireArguments())
        genre = args.genreName
        viewModel.getMoviesByGenre(args.genreID)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("TTT", "onCreateView(")
        binding = ScreenMoviesByGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startShimmerAnimation()
        initButtons()
        setObservers()


    }

    private fun setObservers() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner, productsObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }



    private fun initButtons() {
        adapter = HomeAdapter()
        binding.rvMoviesByGenre.adapter = adapter


        binding.textGenreName.text = genre

        adapter.setItemClickListener { movieID ->
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            val action = MoviesByGenreScreenDirections.actionMoviesByGenreScreenToInfoScreen(movieID)
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

    private val productsObserver = Observer<List<MovieData>> { movies ->
        stopShimmerAnimation()
        updateUi(movies)
    }


    private fun updateUi(movies: List<MovieData>) {
        lifecycleScope.launch {
            adapter.submitList(movies)
        }
    }

    private fun stopShimmerAnimation() {
        binding.shimmerMovies.stopShimmer()
        binding.shimmerMovies.visibility = View.GONE
        binding.rvMoviesByGenre.visibility = View.VISIBLE
    }

    private fun startShimmerAnimation() {
        binding.shimmerMovies.visibility = View.VISIBLE
        binding.shimmerMovies.startShimmer()
        binding.rvMoviesByGenre.visibility = View.GONE
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
        fun newInstance() = MoviesByGenreScreen()
    }
}
