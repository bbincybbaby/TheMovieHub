package me.bincy.movielist.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.bincy.movielist.data.models.Movie
import javax.inject.Inject

class MovieListPagingSource @Inject constructor(
    private val repository: MovieRepository,
    private val query: String?
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            var response = repository.getMovies(currentPage)
            if (query?.isNotBlank() == true) {
                response = response.filter { it.name?.contains(query, true) == true }.map {
                    Movie(it.name,it.posterImage,query)
                }
            }
            LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = if (currentPage < 3) {
                    currentPage.plus(1)
                } else {
                    null
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }


}