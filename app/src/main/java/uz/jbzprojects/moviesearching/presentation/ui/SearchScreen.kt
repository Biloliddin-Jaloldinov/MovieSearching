package uz.jbzprojects.moviesearching.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenSearchBinding
import uz.jbzprojects.moviesearching.network.data.searchedData.SearchedMovies
import uz.jbzprojects.moviesearching.presentation.adapter.SearchedHistoryAdapter
import uz.jbzprojects.moviesearching.presentation.adapter.SearchedMoviesAdapter
import uz.jbzprojects.moviesearching.presentation.dialogs.SearchingParamsDialog
import uz.jbzprojects.moviesearching.presentation.viewmodel.SearchMoviesViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.SearchMoviesViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.SearchingHistoryEntity

class SearchScreen : Fragment() {
    private lateinit var binding: ScreenSearchBinding
    private val viewModel: SearchMoviesViewModel by viewModels<SearchMoviesViewModelImpl>()
    private lateinit var searchAdapter: SearchedMoviesAdapter
    private lateinit var historyAdapter: SearchedHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenSearchBinding.inflate(inflater, container, false)
        viewModel.getSearchingHistory()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner, searchedMoviesObserver)
        viewModel.historiesLiveData.observe(viewLifecycleOwner, searchedHistoryObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun initButtons() {
        searchAdapter = SearchedMoviesAdapter()
        binding.rvSearch.adapter = searchAdapter

        searchAdapter.setItemClickListener { moiveID ->
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            val action = SearchScreenDirections.actionSearchFragmentToInfoScreen(moiveID)
            navController.navigate(action)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    startShimmerAnimation()
                    viewModel.getSearchedMovies(query.trim())
                    viewModel.addHistory(
                        SearchingHistoryEntity(
                            searchedText = query,
                            userID = viewModel.getLoggedUserID()
                        )
                    )
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    if (p0.trim().isEmpty()) {
                        binding.rvSearchHistory.visibility = View.VISIBLE
                    } else {
                        binding.rvSearchHistory.visibility = View.GONE
                    }
                }
                return true
            }
        })


        historyAdapter = SearchedHistoryAdapter()
        binding.rvSearchHistory.adapter = historyAdapter

        historyAdapter.setItemClickListener {
            binding.searchView.setQuery(it, true)
            viewModel.getSearchedMovies(it)
            startShimmerAnimation()
        }

        historyAdapter.setItemLongClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Вы хотите удалить \"${it.searchedText}\" из истории поиска?")
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.deleteSearchingHistory(it.id)
            }
            builder.setNegativeButton("Отмена") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }
        binding.btnFilter.setOnClickListener {

            val params = viewModel.getParamForSearch()
            val dialog = SearchingParamsDialog(params.first, params.second)
            dialog.setSaveClickListener { year, language ->
                viewModel.saveSearchParams(year, language)
            }
            dialog.show(childFragmentManager, "SearchingParamsDialog")

        }

    }


    private val errorObserver = Observer<String> { errorMessage ->
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private val searchedMoviesObserver = Observer<List<SearchedMovies>> { movies ->
        stopShimmerAnimation()
        updateUi(movies)
    }

    private val searchedHistoryObserver = Observer<List<SearchingHistoryEntity>> { histories ->
        historyAdapter.submitList(histories)
    }


    private fun updateUi(movies: List<SearchedMovies>) {
        searchAdapter.submitList(movies)
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
}
