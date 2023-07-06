package me.bincy.movielist.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import me.bincy.movielist.data.MovieListPagingSource
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val pagingSource: MovieListPagingSource) :
    ViewModel() {
    val moviesList = Pager(PagingConfig(1)) {
        pagingSource
    }.flow.cachedIn(viewModelScope)
}