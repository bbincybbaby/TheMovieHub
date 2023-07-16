@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package me.bincy.movielist.ui.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import me.bincy.movielist.data.MovieListPagingSource
import me.bincy.movielist.data.MovieRepository
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    val queryStateFlow: StateFlow<String> =
        savedStateHandle.getStateFlow("query", "")

    fun setQuery(query: String) {
        savedStateHandle["query"] = query
    }

    fun setKeyboardVisibility(visibility: Boolean) {
        savedStateHandle["keyboard-visibility"] = visibility
    }

    fun isKeyBoardVisible(): Boolean = savedStateHandle.get<Boolean>("keyboard-visibility") ?: false

    val moviesList = queryStateFlow.debounce {
        if (it.isNotBlank()) {
            600
        } else {
            0
        }
    }.distinctUntilChanged().filter {
        it.length in 4..19 || it.isEmpty()
    }.onStart { emit("") }.flatMapLatest {
        Pager(PagingConfig(1)) {
            MovieListPagingSource(movieRepository, it)
        }.flow
    }.cachedIn(viewModelScope)
}