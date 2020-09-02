package com.sychev.moviemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sychev.moviemanager.R
import com.sychev.moviemanager.data.db.entity.Movie
import com.sychev.moviemanager.ui.search.SearchFragment
import kotlinx.android.synthetic.main.movie_card_layout.view.*
import kotlinx.android.synthetic.main.movie_card_layout_without_rating.view.*

class WatchlistAdapter(private val listMovie: List<Movie>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WatchedMovieHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_card_layout_without_rating, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WatchedMovieHolder -> {
                holder.initWatchlistCardView(listMovie[position])
            }
        }
    }

    class WatchedMovieHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        val listView = itemView

        var movieTitle = itemView.movie_card_title_watched
        var moviePoster = itemView.movie_card_poster_watched
        var movieDirector = itemView.movie_card_director_watched

        fun initWatchlistCardView(movie: Movie) {

            movieTitle.text = movie.title
            movieDirector.text = movie.director
            Picasso.get()
                .load(movie.poster)
                .into(moviePoster)

            listView.setOnClickListener {

                context.startActivity(SearchFragment().startDetailedMovieActivity(context, movie))

            }

        }


    }

}