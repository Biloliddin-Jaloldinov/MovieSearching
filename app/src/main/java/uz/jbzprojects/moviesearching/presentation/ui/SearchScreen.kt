package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenSearchBinding
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedMovies
import uz.jbzprojects.moviesearching.presentation.adapter.SearchedMoviesAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.SearchMoviesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.SearchMoviesViewModelImpl

class SearchScreen : Fragment() {
    private lateinit var binding: ScreenSearchBinding
    private val viewModel: SearchMoviesViewModel by viewModels<SearchMoviesViewModelImpl>()
    private lateinit var adapter: SearchedMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSearchedMovies("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner, searchedMoviesObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun initButtons() {
        adapter = SearchedMoviesAdapter()
        binding.rvSearch .adapter = adapter

        adapter.setItemClickListener { moiveID ->
            val bundle = Bundle()
            bundle.putInt("MOVIE_ID", moiveID)
            val movieScreenFragment = MovieInfoScreen.newInstance()
            movieScreenFragment.arguments = bundle
            Toast.makeText(context, "Clicked $moiveID", Toast.LENGTH_SHORT).show()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainFragmentContainerView, movieScreenFragment)
            transaction.addToBackStack(null)  // Добавляем в стек, чтобы можно было вернуться
            transaction.commit()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    startShimmerAnimation()
                    viewModel.getSearchedMovies(query.trim())
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return true
            }
        })

    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val searchedMoviesObserver = Observer<List<SearchedMovies>> { movies ->
        stopShimmerAnimation()
        updateUi(movies)
    }


    private fun updateUi(movies: List<SearchedMovies>) {
        adapter.submitList(movies)
    }

    private fun stopShimmerAnimation() {
        binding.shimmerMovies.stopShimmer()
        binding.shimmerMovies.visibility = View.GONE
        binding.rvSearch.visibility = View.VISIBLE
    }

    private fun startShimmerAnimation() {
        binding.shimmerMovies.visibility = View.VISIBLE
        binding.shimmerMovies.startShimmer()
        binding.rvSearch.visibility = View.GONE
    }


    companion object {
        @JvmStatic
        fun newInstance() = SearchScreen()
    }
}
