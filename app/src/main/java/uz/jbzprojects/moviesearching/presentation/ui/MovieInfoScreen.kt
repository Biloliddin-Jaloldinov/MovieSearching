package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenMovieInfoBinding
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.presentation.viewmodel.MovieInfoViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.MovieInfoViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity

class MovieInfoScreen : Fragment() {
    private lateinit var binding: ScreenMovieInfoBinding
    private val viewModel: MovieInfoViewModel by lazy {
        ViewModelProvider(this).get(MovieInfoViewModelImpl::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieID = arguments?.getInt("MOVIE_ID", 1096197)
        /*val args = MovieInfoScreenArgs.fromBundle(requireArguments())
        val movieID = args.movieId*/
        viewModel.getMovieByID(movieID!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenMovieInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startShimmerAnimation()
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.movieLiveData.observe(viewLifecycleOwner, movieInfoObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun initButtons() {
        
    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val movieInfoObserver = Observer<OneMovieData> { movie ->
        stopShimmerAnimation()
        updateUi(movie)
    }


    private fun updateUi(movie: OneMovieData) {
        Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.poster_path}").into(binding.imageMovie)
        binding.ratingBarMovie.rating = (movie.vote_average / 2).toFloat()
        binding.textRating.text = (movie.vote_average / 2).toString().substring(0, 3)
        binding.textMovieDescription.text = movie.overview

        val genres = StringBuilder("Жанры: ")

        for (i in 0 until movie.genres.size) {
            genres.append(movie.genres[i].name + " | ")
        }
        binding.textGenres.text = genres.toString()
        binding.textDateAndSeason.text = movie.release_date
        binding.textTitle.text = movie.title

        if (viewModel.checkMovieToFavourite(movie.id)) {
            binding.btnFavoutite.setImageResource(R.drawable.icon_favorite_selected)
        } else {
            binding.btnFavoutite.setImageResource(R.drawable.icon_favorite_not_selected)
        }

        binding.btnFavoutite.setOnClickListener {
            if (viewModel.checkMovieToFavourite(movie.id)) {
                binding.btnFavoutite.setImageResource(R.drawable.icon_favorite_not_selected)
                viewModel.removeMovieFromFavourites(movie.id)
            } else {
                binding.btnFavoutite.setImageResource(R.drawable.icon_favorite_selected)
                viewModel.addMovieToFavourite(
                    FavouriteMovieEntity(
                        movieId = movie.id,
                        isFavourite = true,
                        image = movie.poster_path,
                        title = movie.title,
                        date = movie.release_date
                    )
                )
            }
        }
    }

    private fun stopShimmerAnimation() {
        binding.shimmerMovieInfo.stopShimmer()
        binding.shimmerMovieInfo.visibility = View.GONE
        binding.infoScreeUI.visibility = View.VISIBLE
    }

    private fun startShimmerAnimation() {
        binding.shimmerMovieInfo.visibility = View.VISIBLE
        binding.shimmerMovieInfo.startShimmer()
        binding.infoScreeUI.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = MovieInfoScreen()
    }

}
