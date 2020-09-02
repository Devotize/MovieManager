package com.sychev.moviemanager.ui.watched

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sychev.moviemanager.R
import com.sychev.moviemanager.adapter.WatchedAdapter
import com.sychev.moviemanager.data.db.entity.Movie
import com.sychev.moviemanager.data.db.repository.MovieRepository
import com.sychev.moviemanager.data.response.MovieStatus
import com.sychev.moviemanager.decoration.Decoration
import kotlinx.android.synthetic.main.watched_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class WatchedFragment : Fragment() {

    private lateinit var movieRepository: MovieRepository
    private lateinit var movieAdapter: WatchedAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.watched_fragment, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        movieRepository = MovieRepository(context!!)

        CoroutineScope(Main).launch {
            initRecyclerView()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager: SearchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = menuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                movieAdapter.filter.filter(newText)
                println("Debug on query text change: $newText")

                return false
            }

        })

    }

    private suspend fun initRecyclerView() {

        viewManager = LinearLayoutManager(context)
        movieAdapter = WatchedAdapter(getWatchedMovies())
        watched_movies_recycler_view.apply {
            layoutManager = viewManager
            adapter = movieAdapter
            addItemDecoration(Decoration(10))
        }
    }

    private suspend fun getWatchedMovies(): List<Movie> {

        return movieRepository.getMoviesByStatus(MovieStatus().watched)

    }


}