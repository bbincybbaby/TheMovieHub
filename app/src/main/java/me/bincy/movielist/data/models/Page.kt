package me.bincy.movielist.data.models

import com.squareup.moshi.Json

data class Page(
    @Json(name = "content-items")
    var movieItems: MovieList?,
    @Json(name = "page-num")
    var pageNum: String?,
    @Json(name = "page-size")
    var pageSize: String?,
    @Json(name = "title")
    var title: String?,
    @Json(name = "total-content-items")
    var totalContentItems: String?
)