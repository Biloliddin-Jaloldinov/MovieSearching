package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenGenresBinding
import uz.jbzprojects.moviesearching.network.data.genresData.GenreData
import uz.jbzprojects.moviesearching.presentation.adapter.GenresAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.GenresViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.GenresViewModelImpl

class GenresScreen : Fragment() {
    private lateinit var binding: ScreenGenresBinding
    private val viewModel: GenresViewModel by lazy {
        ViewModelProvider(this)[GenresViewModelImpl::class.java]
    }
    private lateinit var adapter: GenresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMoviesGenres()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("TTT", "onCreateView(")
        binding = ScreenGenresBinding.inflate(inflater, container, false)
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
        adapter = GenresAdapter()
        binding.rvGenres.adapter = adapter

        adapter.setItemClickListener { genre ->

            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            val action = GenresScreenDirections.actionGenresScreenToMoviesByGenreScreen(genre.id.toString(), genre.name)
            navController.navigate(action)
        }

    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val productsObserver = Observer<List<GenreData>> { genres ->
        stopShimmerAnimation()
        updateUi(genres.sortedBy { it.name })
    }


    private fun updateUi(movies: List<GenreData>) {
        lifecycleScope.launch {
            adapter.submitList(movies)
        }
    }

    private fun stopShimmerAnimation() {
        binding.shimmerGenres.stopShimmer()
        binding.shimmerGenres.visibility = View.GONE
        binding.rvGenres.visibility = View.VISIBLE
    }

    private fun startShimmerAnimation() {
        binding.shimmerGenres.visibility = View.VISIBLE
        binding.shimmerGenres.startShimmer()
        binding.rvGenres.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = GenresScreen()
    }
}
