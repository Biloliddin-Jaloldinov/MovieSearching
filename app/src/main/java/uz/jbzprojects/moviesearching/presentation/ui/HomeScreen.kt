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
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenHomeBinding
import uz.jbzprojects.moviesearching.network.data.maindata.MovieData
import uz.jbzprojects.moviesearching.presentation.adapter.HomeAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.HomeViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.HomeViewModelImpl

class HomeScreen : Fragment() {
    private lateinit var binding: ScreenHomeBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModelImpl::class.java)
    }
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TTT", "Main Screen")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("TTT", "onCreateView(")
        binding = ScreenHomeBinding.inflate(inflater, container, false)
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
        binding.rv.adapter = adapter

        adapter.setItemClickListener { moiveID ->

            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            val action = HomeScreenDirections.actionHomeFragmentToInfoScreen(moiveID)
            navController.navigate(action)
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
        binding.rv.visibility = View.VISIBLE
    }

    private fun startShimmerAnimation() {
        binding.shimmerMovies.visibility = View.VISIBLE
        binding.shimmerMovies.startShimmer()
        binding.rv.visibility = View.GONE
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeScreen()
    }

}
