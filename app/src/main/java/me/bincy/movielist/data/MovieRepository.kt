package me.bincy.movielist.data

import me.bincy.movielist.data.models.Movie

interface MovieRepository {
    suspend fun getMovies(page: Int): List<Movie>
}