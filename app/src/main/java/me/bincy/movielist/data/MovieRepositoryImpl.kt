package me.bincy.movielist.data

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.bincy.movielist.R
import me.bincy.movielist.data.models.Movie
import me.bincy.movielist.data.models.MoviesPage

class MovieRepositoryImpl(private val moshi: Moshi, private val context: Context) :
    MovieRepository {

    override suspend fun getMovies(page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            val jsonFile = when (page) {
                1 -> R.raw.content_page_1
                2 -> R.raw.content_page_2
                3 -> R.raw.content_page_3
                else -> null
            }
            val jsonString = try {
                jsonFile?.let { data ->
                    context.resources.openRawResource(data)
                        .bufferedReader().use { it.readText() }
                }
            } catch (_:Exception){
                null
            }

            val adapter: JsonAdapter<MoviesPage> = moshi.adapter(MoviesPage::class.java)
            val moviesPage = jsonString?.let { adapter.fromJson(it) }
            moviesPage?.page?.movieItems?.movie
        } ?: emptyList()
    }
}