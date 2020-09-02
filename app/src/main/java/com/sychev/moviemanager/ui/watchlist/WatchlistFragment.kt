package com.sychev.moviemanager.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sychev.moviemanager.R
import com.sychev.moviemanager.adapter.WatchedAdapter
import com.sychev.moviemanager.adapter.WatchlistAdapter
import com.sychev.moviemanager.data.db.entity.Movie
import com.sychev.moviemanager.data.db.repository.MovieRepository
import com.sychev.moviemanager.data.response.MovieStatus
import com.sychev.moviemanager.decoration.Decoration
import kotlinx.android.synthetic.main.watchlist_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WatchlistFragment: Fragment() {

    private lateinit var movieRepository: MovieRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.watchlist_fragment, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieRepository = MovieRepository(context!!)

        CoroutineScope(Main).launch {
            initRecyclerView()
        }



    }

    private suspend fun initRecyclerView() {
        movie_watchlist_recycler_view.apply {
            adapter = WatchlistAdapter(getWatchlistMovies())
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(Decoration(5))
        }
    }

    private suspend fun getWatchlistMovies(): List<Movie> {
        return movieRepository.getMoviesByStatus(MovieStatus().watchlist)
    }

}