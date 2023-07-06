package me.bincy.movielist.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.bincy.movielist.R
import me.bincy.movielist.databinding.ItemMovieLayoutBinding
import me.bincy.movielist.data.models.Movie
import javax.inject.Inject

class SearchMovieListAdapter @Inject constructor() :
    ListAdapter<Movie, SearchMovieListAdapter.MovieItemViewHolder>(differCallback) {


    class MovieItemViewHolder(private val binding: ItemMovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(movie: Movie) {
            binding.movieTitleTextView.text = movie.name
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