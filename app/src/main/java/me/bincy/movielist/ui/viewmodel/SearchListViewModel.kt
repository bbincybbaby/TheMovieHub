package me.bincy.movielist.ui.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import me.bincy.movielist.data.MovieRepository
import javax.inject.Inject

@HiltViewModel
class SearchListViewModel @Inject constructor(
    private val searchRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    val queryData: StateFlow<String> =
        savedStateHandle.getStateFlow("query", "")

    fun setQuery(query: String) {
        savedStateHandle["query"] = query
    }

    val searchResults = queryData
        .debounce(600)
        .distinctUntilChanged()
        .map {
            searchRepository.searchMovies(it)
        }
}