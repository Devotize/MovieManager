package com.sychev.moviemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sychev.moviemanager.R
import com.sychev.moviemanager.data.db.entity.Movie
import com.sychev.moviemanager.ui.search.SearchFragment
import kotlinx.android.synthetic.main.movie_card_layout.view.*
import java.util.*
import java.util.logging.Filter
import kotlin.collections.ArrayList

class WatchedAdapter(private var moviesList: List<Movie> = ArrayList<Movie>())
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var movieFilterList:List<Movie> = ArrayList<Movie>()
    init {
        movieFilterList = moviesList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyMovieHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.movie_card_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyMovieHolder -> {
                holder.initWatchedCardView(movieFilterList[movieFilterList.size - position - 1])
                println("debug position: $position")
            }
        }

    }

    override fun getItemCount(): Int {
        return movieFilterList.size
    }

    class MyMovieHolder(
        cardView: View
    ): RecyclerView.ViewHolder(cardView) {
        val context = cardView.context
        val card = cardView

        var movieTitle = cardView.movie_card_title
        var moviePoster = cardView.movie_card_poster
        var movieUserRating = cardView.movie_card_user_rating
        var movieDirector = cardView.movie_card_director

        fun initWatchedCardView(movie: Movie) {

            movieTitle.text = movie.title
            movieDirector.text = context.getString(R.string.director, movie.director)
            movieUserRating.text =
                context.getString(R.string.user_rating_text, if (movie.userRating == null) "None" else movie.userRating)
            Picasso.get()
                .load(movie.poster)
                .into(moviePoster)

            card.setOnClickListener {

                context.startActivity(SearchFragment().startDetailedMovieActivity(context, movie))
            }

        }


    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                movieFilterList = if (charSearch.isEmpty()) {
                    moviesList
                } else {
                    val resultList = ArrayList<Movie>()
                    for (row in moviesList) {
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResult = FilterResults()
                filterResult.values = movieFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movieFilterList = results?.values as ArrayList<Movie>
                notifyDataSetChanged()
            }

        }
    }


}