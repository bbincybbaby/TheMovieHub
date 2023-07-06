package me.bincy.movielist.data.models

import com.squareup.moshi.Json

data class Movie(
    @Json(name = "name")
    var name: String?,
    @Json(name = "poster-image")
    var posterImage: String?
)