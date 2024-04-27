package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import uz.jbzprojects.moviesearching.databinding.ScreenFavouritesBinding
import uz.jbzprojects.moviesearching.presentation.adapter.FavouritesAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.FavouritesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.FavouritesViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

class FavouritesScreen : Fragment() {
    private lateinit var binding: ScreenFavouritesBinding
    private val viewModel: FavouritesViewModel by viewModels<FavouritesViewModelImpl>()
    private lateinit var adapter: FavouritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startShimmerAnimation()
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.moviesLiveData.observe(this, productsObserver)
        viewModel.error.observe(this, errorObserver)
    }

    private fun initButtons() {
        adapter = FavouritesAdapter()
        binding.rvFavourites.adapter = adapter

        adapter.setItemClickListener { moiveID ->
            val bundle = Bundle()
            bundle.putInt("MOVIE_ID", moiveID)
            val movieScreenFragment = MovieInfoScreen.newInstance()
            movieScreenFragment.arguments = bundle
            Toast.makeText(context, "Clicked $moiveID", Toast.LENGTH_SHORT).show()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(this.id, movieScreenFragment)
            transaction.addToBackStack(null)  // Добавляем в стек, чтобы можно было вернуться
            transaction.commit()
        }

    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val productsObserver = Observer<List<FavouriteMovieEntity>> { movies ->
        stopShimmerAnimation()
        updateUi(movies)
    }


    private fun updateUi(movies: List<FavouriteMovieEntity>) {
        adapter.submitList(movies)
    }

    private fun stopShimmerAnimation() {
  /*      binding.shimmerMovies.stopShimmer()
        binding.shimmerMovies.visibility = View.GONE
        binding.rv.visibility = View.VISIBLE*/
    }

    private fun startShimmerAnimation() {
       /* binding.shimmerMovies.visibility = View.VISIBLE
        binding.shimmerMovies.startShimmer()
        binding.rv.visibility = View.GONE*/
    }


    companion object {
        @JvmStatic
        fun newInstance() = FavouritesScreen()
    }
}
