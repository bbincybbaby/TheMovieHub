package me.bincy.movielist.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.bincy.movielist.data.models.Movie
import javax.inject.Inject

class MovieListPagingSource @Inject constructor(
    private val repository: MovieRepository
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getMovies(currentPage)
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