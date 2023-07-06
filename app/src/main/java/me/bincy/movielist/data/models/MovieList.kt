package me.bincy.movielist.data.models

import com.squareup.moshi.Json

data class MovieList(
    @Json(name = "content")
    var movie: List<Movie>?
)