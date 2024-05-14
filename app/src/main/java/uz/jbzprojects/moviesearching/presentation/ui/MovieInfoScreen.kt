package uz.jbzprojects.moviesearching.presentation.ui

import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.LinearSnapHelper
import com.squareup.picasso.Picasso
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenMovieInfoBinding
import uz.jbzprojects.moviesearching.network.data.moviedata.OneMovieData
import uz.jbzprojects.moviesearching.presentation.adapter.ReviewsAdapter
import uz.jbzprojects.moviesearching.presentation.dialogs.AddReviewDialog
import uz.jbzprojects.moviesearching.presentation.dialogs.EditReviewDialog
import uz.jbzprojects.moviesearching.presentation.viewmodel.MovieInfoViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.MovieInfoViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.FavouriteMovieEntity
import uz.jbzprojects.moviesearching.storage.room.entity.ReviewEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchLaterEntity
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovieInfoScreen : Fragment() {
    private lateinit var binding: ScreenMovieInfoBinding
    private val viewModel: MovieInfoViewModel by viewModels<MovieInfoViewModelImpl>()

    private lateinit var adapter: ReviewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = MovieInfoScreenArgs.fromBundle(requireArguments())
        val movieID = args.movieId
        viewModel.getMovieByID(movieID)
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
        viewModel.reviewsLiveData.observe(viewLifecycleOwner, reviewsInfoObserver)
        viewModel.userReviewLiveData.observe(viewLifecycleOwner, userReviewObserver)
    }

    private fun initButtons() {
        val isAdmin = viewModel.getLoggedID() == 1
        adapter = ReviewsAdapter(isAdmin)
        binding.rvReviews.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvReviews)

        binding.btnBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.popBackStack()
        }

        adapter.setDeleteButtonListener { review ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Вы хотите удалить свой отзыв?")
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.deleteReview(review)
            }
            builder.setNegativeButton("Отмена") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }

    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val movieInfoObserver = Observer<OneMovieData> { movie ->
        stopShimmerAnimation()
        updateUi(movie)
    }
    private val reviewsInfoObserver = Observer<List<ReviewEntity>> { reviews ->
        adapter.submitList(reviews)
    }

    private val userReviewObserver = Observer<ReviewEntity?> { review ->
        if (review == null) {
            binding.addRatingForMovie.visibility = View.VISIBLE
            binding.userReview.visibility = View.GONE
            binding.textAddReview.text = "Оставьте отзыв"
        } else {
            binding.addRatingForMovie.visibility = View.GONE
            binding.userReview.visibility = View.VISIBLE
            binding.textAddReview.text = "Ваш отзыв"
            updateUserReview(review)
        }
        binding.btnDeleteReview.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Вы хотите удалить свой отзыв?")
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.deleteUserReview(review!!)
            }
            builder.setNegativeButton("Отмена") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.btnEditReview.setOnClickListener {
            val editDialog = EditReviewDialog(review!!)
            editDialog.setUpdateClickListener {
                viewModel.updateUserReview(review)
            }
            editDialog.show(childFragmentManager, "editDialog")
        }
    }

    private fun updateUserReview(review: ReviewEntity) {
        binding.imgUser.setImageResource(review.imageUser)
        binding.textUerName.text = review.userName
        binding.textDate.text = review.date
        binding.textRatingForUser.text = review.rating
        binding.textComment.text = review.comment

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
            binding.btnFavourite.setImageResource(R.drawable.icon_favorite_selected)
        } else {
            binding.btnFavourite.setImageResource(R.drawable.icon_favorite_not_selected)
        }

        binding.btnFavourite.setOnClickListener {
            if (viewModel.checkToLoggedIn()) {
                if (viewModel.checkMovieToFavourite(movie.id)) {
                    binding.btnFavourite.setImageResource(R.drawable.icon_favorite_not_selected)
                    viewModel.removeMovieFromFavourites(movie.id)
                } else {
                    binding.btnFavourite.setImageResource(R.drawable.icon_favorite_selected)
                    if (viewModel.checkToLoggedIn())
                        viewModel.addMovieToFavourite(
                            FavouriteMovieEntity(
                                movieId = movie.id,
                                isFavourite = true,
                                image = movie.poster_path,
                                title = movie.title,
                                date = movie.release_date,
                                userID = -1
                            )
                        )
                }
            } else {
                Toast.makeText(requireContext(), "Войдити чтобы добавлять в избранные", Toast.LENGTH_SHORT).show()
            }
        }
        if (viewModel.checkMovieToFavourite(movie.id)) {
            binding.btnFavourite.setImageResource(R.drawable.icon_favorite_selected)
        } else {
            binding.btnFavourite.setImageResource(R.drawable.icon_favorite_not_selected)
        }

        binding.btnFavourite.setOnClickListener {
            if (viewModel.checkToLoggedIn()) {
                if (viewModel.checkMovieToFavourite(movie.id)) {
                    binding.btnFavourite.setImageResource(R.drawable.icon_favorite_not_selected)
                    viewModel.removeMovieFromFavourites(movie.id)
                } else {
                    binding.btnFavourite.setImageResource(R.drawable.icon_favorite_selected)
                    if (viewModel.checkToLoggedIn())
                        viewModel.addMovieToFavourite(
                            FavouriteMovieEntity(
                                movieId = movie.id,
                                isFavourite = true,
                                image = movie.poster_path,
                                title = movie.title,
                                date = movie.release_date,
                                userID = -1
                            )
                        )
                }
            } else {
                Toast.makeText(requireContext(), "Войдити чтобы добавлять в избранные", Toast.LENGTH_SHORT).show()
            }
        }
        if (viewModel.checkMovieToWatchLater(movie.id)) {
            binding.btnWatchLater.setImageResource(R.drawable.icon_bookmark_selected)
        } else {
            binding.btnWatchLater.setImageResource(R.drawable.icon_bookmark_not_selected)
        }

        binding.btnWatchLater.setOnClickListener {
            if (viewModel.checkToLoggedIn()) {
                if (viewModel.checkMovieToWatchLater(movie.id)) {
                    binding.btnWatchLater.setImageResource(R.drawable.icon_bookmark_not_selected)
                    viewModel.removeMovieFromWatchLater(movie.id)
                } else {
                    binding.btnWatchLater.setImageResource(R.drawable.icon_bookmark_selected)
                    if (viewModel.checkToLoggedIn())
                        viewModel.addMovieToWatchLater(
                            WatchLaterEntity(
                                movieId = movie.id,
                                isWatchLater = true,
                                image = movie.poster_path,
                                title = movie.title,
                                date = movie.release_date,
                                userID = -1
                            )
                        )
                }
            } else {
                Toast.makeText(requireContext(), "Войдити чтобы добавлять в смотреть позже", Toast.LENGTH_SHORT).show()
            }
        }
        Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.backdrop_path}").into(binding.imageForPlayer)
        binding.btnToBrowser.setOnClickListener {
            if (movie.homepage.isEmpty()) {
                Toast.makeText(requireContext(), "Страница не найдена", Toast.LENGTH_SHORT).show()
            } else {
                val url = movie.homepage
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }

        if (movie.production_countries.isEmpty()) {
            binding.textProductionCountry.text = "Страна производства : —"
        } else {
            binding.textProductionCountry.text = "Страна производства : " + movie.production_countries[0].name
        }
        if (movie.spoken_languages.isEmpty()) {
            binding.textSpokenLanguage.text = "Язык озвучки : —"
        } else {
            binding.textSpokenLanguage.text = "Язык озвучки : " + movie.spoken_languages[0].english_name
        }

        binding.addRatingForMovie.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if (viewModel.checkToLoggedIn()) {
                val addReviewDialog = AddReviewDialog()
                val userInfo = viewModel.getUserInfo()
                addReviewDialog.setSaveClickListener { rating, comment ->
                    viewModel.addUserReview(
                        ReviewEntity(
                            userID = userInfo.userID,
                            userName = userInfo.userName,
                            imageUser = userInfo.image,
                            date = getCurrentDateAntTime(),
                            rating = rating,
                            comment = comment,
                            movieTitle = movie.title,
                            movieID = movie.id,
                            movieImage = movie.poster_path
                        )
                    )
                    addReviewDialog.dismiss()
                }
                addReviewDialog.show(childFragmentManager, "addReviewDialog")
            } else {
                Toast.makeText(requireContext(), "Войдити чтобы оставить отзыв", Toast.LENGTH_SHORT).show()
            }
            binding.addRatingForMovie.rating = 0f
        }

        viewModel.getUserReview(movie.id)
        if (viewModel.getLoggedID() != -1) {
            viewModel.addToWatchedHistory(
                WatchedHistoryEntity(
                    movieId = movie.id,
                    image = movie.poster_path,
                    title = movie.title,
                    watchedDate = getCurrentDateAntTime(),
                    userID = viewModel.getLoggedID()
                )
            )
        }
    }

    private fun getCurrentDateAntTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())

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
        fun newInstance() = MovieInfoScreen()
    }

}
