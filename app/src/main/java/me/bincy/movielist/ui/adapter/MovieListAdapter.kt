package me.bincy.movielist.ui.adapter

import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.bincy.movielist.R
import me.bincy.movielist.data.models.Movie
import me.bincy.movielist.databinding.ItemMovieLayoutBinding
import javax.inject.Inject

class MovieListAdapter @Inject constructor() :
    PagingDataAdapter<Movie, MovieListAdapter.MovieItemViewHolder>(differCallback) {

    class MovieItemViewHolder(private val binding: ItemMovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(movie: Movie) {
            if (movie.queryData?.isNotBlank() == true){
                val builder = SpannableStringBuilder(movie.name)
                movie.queryData?.let { queryString ->
                    movie.name?.indexOf(queryString, ignoreCase = true)?.let { index ->
                        builder.setSpan(
                            ForegroundColorSpan(Color.YELLOW),
                            index,
                            queryString.length + index,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                binding.movieTitleTextView.setText(builder, TextView.BufferType.SPANNABLE)
            } else{
                binding.movieTitleTextView.text = movie.name
            }
            Glide.with(binding.movieImageView.context)
                .load(Uri.parse("file:///android_asset/${movie.posterImage}"))
                .placeholder(R.drawable.img_empty)
                .into(binding.movieImageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding: ItemMovieLayoutBinding =
            ItemMovieLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bindData(it) }
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}