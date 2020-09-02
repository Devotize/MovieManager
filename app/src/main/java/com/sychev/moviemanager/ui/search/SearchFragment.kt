package com.sychev.moviemanager.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.sychev.moviemanager.R
import com.sychev.moviemanager.data.db.entity.Movie
import com.sychev.moviemanager.data.db.repository.MovieRepository
import com.sychev.moviemanager.data.response.MovieSearchResponse
import com.sychev.moviemanager.data.response.MovieStatus
import com.sychev.moviemanager.data.service.MovieCall
import com.sychev.moviemanager.ui.DetailedMovieActivity
import com.sychev.moviemanager.ui.EXTRA_MOVIE
import kotlinx.android.synthetic.main.search_fragment.*


class SearchFragment: Fragment() {

    private lateinit var movieCallService: MovieCall
    private lateinit var movieRepository: MovieRepository
    val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.search_fragment, container, false)

//        val context: Context = activity as AppCompatActivity

        movieCallService = MovieCall()
        movieRepository = MovieRepository(context!!)

        return view
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager: SearchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = menuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                movie_information_main_layout.visibility = View.INVISIBLE

                hideKeyboard(searchView)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }


    private fun hideKeyboard(searchView: SearchView) {
        val inputMethodManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
        searchView.clearFocus()

    }

    private fun fromMovieSearchResponseToMovie(movieSearchResponse: MovieSearchResponse?): Movie {
        return Movie(
            movieSearchResponse!!.title,
            movieSearchResponse.year,
            movieSearchResponse.released,
            movieSearchResponse.runtime,
            movieSearchResponse.genre,
            movieSearchResponse.director,
            movieSearchResponse.writer,
            movieSearchResponse.actors,
            movieSearchResponse.country,
            movieSearchResponse.plot,
            movieSearchResponse.awards,
            movieSearchResponse.poster,
            movieSearchResponse.imdbRating,
            movieSearchResponse.boxOffice,
            MovieStatus().undefined,
            null
        )
    }

    private fun performSearch(term: String?) {


        movieCallService.searchMovieByTerm(term!!) {

            if (it?.title == null) {
                Toast.makeText(context, "Found Nothing with your $term", Toast.LENGTH_LONG).show()
            } else {
                initMovieLayout(fromMovieSearchResponseToMovie(it))
            }
//            CoroutineScope(IO).launch {
//                println("Debug DatabaseInput: ${fromMovieSearchResponseToMovie(it)}")
//                movieRepository.addMovieToDatabase(fromMovieSearchResponseToMovie(it))
//            }

//            CoroutineScope(Main).launch {
//                println("Debug DatabaseOutput: ${movieRepository.getAllMovies()}")
//            }


        }


    }

    private fun initMovieLayout(currentMovie: Movie) {

        val imageUrl = currentMovie.poster

        movie_title_text_view.text = currentMovie.title
        movie_year_text_view.text = getString(R.string.year_parentheses, "${currentMovie.year}")
        movie_rating_text_view.text = currentMovie.imdbRating
        movie_director_text_view.text = getString(R.string.director, "${currentMovie.director}")
        movie_writer_image_view.text = getString(R.string.writer, "${currentMovie.writer}")
        movie_stars_text_view.text = getString(R.string.stars, "${currentMovie.actors}")
        Picasso.get()
            .load(imageUrl)
            .into(movie_poster_image_view)

        movie_information_main_layout.visibility = View.VISIBLE

        movie_information_main_layout.setOnClickListener {
            startActivity(startDetailedMovieActivity(context!!, currentMovie))
        }

    }

    fun startDetailedMovieActivity(context: Context, movie: Movie): Intent {
        val intent = Intent(context, DetailedMovieActivity::class.java)
        intent.putExtra(EXTRA_MOVIE, movie)
        return intent
    }


}