package me.bincy.movielist.data.models

import com.squareup.moshi.Json

data class MoviesPage(
    @Json(name = "page")
    var page: Page?
)