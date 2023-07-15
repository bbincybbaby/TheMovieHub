package me.bincy.movielist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.bincy.movielist.databinding.FragmentMovieListBinding
import me.bincy.movielist.ui.adapter.MovieListAdapter
import me.bincy.movielist.ui.viewmodel.MovieListViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var binding: FragmentMovieListBinding? = null
    private val viewModel: MovieListViewModel by activityViewModels()

    @Inject
    lateinit var moviesAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orientation = activity?.resources?.configuration?.orientation
        val gridLayoutManger = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 7)
        } else {
            GridLayoutManager(context, 3)
        }
        binding?.movieListRecyclerView?.apply {
            layoutManager = gridLayoutManger
            moviesAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = moviesAdapter
        }

        lifecycleScope.launch {
            viewModel.moviesList.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collectLatest {
                    moviesAdapter.submitData(it)
                }
        }
    }
}