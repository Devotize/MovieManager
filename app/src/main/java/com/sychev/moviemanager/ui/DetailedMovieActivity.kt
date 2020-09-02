package com.sychev.moviemanager.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.squareup.picasso.Picasso
import com.sychev.moviemanager.R
import com.sychev.moviemanager.adapter.WatchedAdapter
import com.sychev.moviemanager.data.db.entity.Movie
import com.sychev.moviemanager.data.db.repository.MovieRepository
import com.sychev.moviemanager.data.response.MovieStatus
import com.sychev.moviemanager.ui.search.SearchFragment
import com.sychev.moviemanager.ui.watched.WatchedFragment
import kotlinx.android.synthetic.main.activity_detailed_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class DetailedMovieActivity : AppCompatActivity() {

    private lateinit var movie: Movie
    private lateinit var movieRepository: MovieRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_movie)
        movieRepository = MovieRepository(this)



        initDetailLayout()
        readUserScoreInput()

    }

    override fun onBackPressed() {
        startMainActivity()
    }

    private fun initDetailLayout() {
        movie = intent.extras?.get(EXTRA_MOVIE) as Movie

        delete_movie_button.setOnClickListener {
            deleteMovieClicked()
        }

        initBackButton()



        Picasso.get()
            .load(movie.poster)
            .into(detail_movie_poster)
        detail_movie_title.text = movie.title
        detail_movie_year.text = getString(R.string.year_parentheses, movie.year)
        detail_movie_director.text = getString(R.string.director, movie.director)
        detail_movie_writer.text = getString(R.string.writer, movie.writer)
        detail_movie_released.text = getString(R.string.released, movie.released)
        detail_movie_country.text = getString(R.string.county, movie.country)
        detail_movie_box_office.text = getString(R.string.boxOffice, movie.boxOffice)
        detail_movie_runtime.text = getString(R.string.runtime, movie.runtime)
        detail_movie_genre.text = getString(R.string.genre, movie.genre)
        detail_movie_awards.text = getString(R.string.awards, movie.awards)
        detail_movie_rating.text = getString(R.string.rating, movie.imdbRating)
        detail_movie_stars.text = getString(R.string.stars, movie.actors)
        detail_movie_plot.text = getString(R.string.plot, movie.plot)

        when (movie.status) {
            null -> {
                detail_movie_user_score_layout.visibility = View.GONE
                showAddButton()
                initAddMovieMenu(R.menu.add_movie_menu)
            }
            MovieStatus().watched -> {
                showDeleteButton()
                initAddMovieMenu(R.menu.add_movie_menu)
            }
            MovieStatus().watchlist -> {
                showAddDeleteButtons()
                initAddMovieMenu(R.menu.add_movie_menu_watchlist)

            }
        }


    }

    private fun initBackButton() {
        previous_activity_button.setOnClickListener {
//            onBackPressed()

            startMainActivity()

        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun initAddMovieMenu(movieMenuResource: Int) {

        add_movie_button.setOnClickListener {
            showPopUpMenu(movieMenuResource)
        }


    }

    private fun showPopUpMenu(movieMenuResource: Int) {
        val popupMenu: PopupMenu = PopupMenu(this, add_movie_button)

        popupMenu.inflate(movieMenuResource)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.add_to_watched -> {
                    addToWatched(movie)
                }
                R.id.add_to_watchlist -> {
                    addToWatchlist(movie)
                }
            }
            true
        })
        popupMenu.show()
    }

    private fun addToWatched(movie: Movie) {
        val movieStatus: MovieStatus = MovieStatus()

        movie.status = movieStatus.watched
        CoroutineScope(IO).launch {
            movieRepository.addMovieToDatabase(movie)
        }
    }

    private fun addToWatchlist(movie: Movie) {
        val movieStatus = MovieStatus()

        movie.status = movieStatus.watchlist
        CoroutineScope(IO).launch {
            movieRepository.addMovieToDatabase(movie)
        }
    }

    private fun readUserScoreInput() {
        val editText = detail_movie_user_score_edit_text

        if (movie.userRating != null) {
            editText.visibility = View.GONE
            detail_movie_user_score_text_view.visibility = View.VISIBLE
        }

        editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    editText.visibility = View.GONE
                    detail_movie_user_score_text_view.visibility = View.VISIBLE

                    CoroutineScope(Main).launch {
                        movie.userRating = editText.text.toString()
                        movieRepository.addMovieToDatabase(movie)
                    }

                    CoroutineScope(Main).launch {
                        detail_movie_user_score_text_view.text =
                            if (movie.userRating == null) "" else movie.userRating
                    }

                    return true
                }
                return false
            }

        })

        detail_movie_user_score_text_view.setOnClickListener {
            editText.visibility = View.VISIBLE
            detail_movie_user_score_text_view.visibility = View.GONE
        }

    }

    private fun deleteMovieClicked() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to remove ${movie.title} from ${movie.status} movies?")
            .setPositiveButton("Yes"
            ) { _, _ ->
                CoroutineScope(IO).launch {
                    movieRepository.deleteMovie(movie)
                    WatchedAdapter().notifyDataSetChanged()
                }

                add_movie_button.visibility = View.VISIBLE
                delete_movie_button.visibility = View.GONE

            }
            .setNegativeButton("No") {_, _ ->
                false
            }
            .create()
            .show()
    }

    private fun showAddDeleteButtons() {
        delete_movie_button.visibility = View.VISIBLE
        add_movie_button.visibility = View.VISIBLE
        val constraintSet = ConstraintSet()
        constraintSet.clone(findViewById<ConstraintLayout>(R.id.detail_layout_main))
        constraintSet.connect(R.id.add_movie_button, ConstraintSet.END, R.id.delete_movie_button, ConstraintSet.START)
        constraintSet.connect(R.id.add_movie_button, ConstraintSet.TOP, R.id.detail_layout_main, ConstraintSet.TOP)
        constraintSet.applyTo(findViewById<ConstraintLayout>(R.id.detail_layout_main))
    }

    private fun showAddButton() {
        delete_movie_button.visibility = View.GONE
        add_movie_button.visibility = View.VISIBLE
    }

    private fun showDeleteButton() {
        delete_movie_button.visibility = View.VISIBLE
        add_movie_button.visibility = View.GONE
    }



}
