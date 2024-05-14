package uz.jbzprojects.moviesearching.presentation.ui

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
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.ScreenWatchedHistoryBinding
import uz.jbzprojects.moviesearching.presentation.adapter.FavouritesAdapter
import uz.jbzprojects.moviesearching.presentation.adapter.WatchedHistoryAdapter
import uz.jbzprojects.moviesearching.presentation.viewmodel.WatchedHistoryViewModel
import uz.jbzprojects.moviesearching.presentation.viewmodel.impl.WatchedHistoryViewModelImpl
import uz.jbzprojects.moviesearching.storage.room.entity.WatchedHistoryEntity

class WatchedHistoryScreen : Fragment() {
    private lateinit var binding: ScreenWatchedHistoryBinding
    private val viewModel: WatchedHistoryViewModel by viewModels<WatchedHistoryViewModelImpl>()
    private lateinit var adapter: WatchedHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ScreenWatchedHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        setObservers()
    }

    private fun setObservers() {
        viewModel.watchedHistoryLiveData.observe(viewLifecycleOwner, productsObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
    }

    private fun initButtons() {
        adapter = WatchedHistoryAdapter()
        binding.rvFavourites.adapter = adapter

        adapter.setItemClickListener { moiveID ->
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            val action = FavouritesScreenDirections.actionFavouritesFragmentToInfoScreen(moiveID)
            navController.navigate(action)
        }

        binding.btnBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContainerView)
            navController.popBackStack()
        }

        binding.btnClearHistory.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Вы хотите очистить историю просмотров?")
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.clearWatchedHistory()
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

    private val productsObserver = Observer<List<WatchedHistoryEntity>> { history ->
        updateUi(history)
    }


    private fun updateUi(watchedHistory: List<WatchedHistoryEntity>) {
        adapter.submitList(watchedHistory)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getHistory()
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
        fun newInstance() = WatchedHistoryScreen()
    }
}
