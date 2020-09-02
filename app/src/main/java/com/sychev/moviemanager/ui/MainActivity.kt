package com.sychev.moviemanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.sychev.moviemanager.R
import com.sychev.moviemanager.data.db.repository.MovieRepository
import com.sychev.moviemanager.data.response.MovieStatus
import com.sychev.moviemanager.data.service.MovieCall
import com.sychev.moviemanager.ui.search.SearchFragment
import com.sychev.moviemanager.ui.watched.WatchedFragment
import com.sychev.moviemanager.ui.watchlist.WatchlistFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

const val EXTRA_MOVIE = "Extra Movie"

class MainActivity : AppCompatActivity() {

    private lateinit var movieCallService: MovieCall
    private lateinit var movieRepository: MovieRepository
    val TAG = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        movieCallService = MovieCall()
        movieRepository = MovieRepository(this)

        initNavigationBottomMenu()

        replaceFragment(SearchFragment())



    }




    private fun AppCompatActivity.replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.host, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun initNavigationBottomMenu() {
        val menu: Menu = navigation_bottom_menu.menu
        menuInflater.inflate(R.menu.navigation_menu, menu)

        val searchFragmentItem = menu.findItem(R.id.searchFragment)
        val watchedFragmentItem = menu.findItem(R.id.watchedFragment)
        val watchlistFragmentItem = menu.findItem(R.id.watchlistFragment)

        searchFragmentItem.setOnMenuItemClickListener {
            replaceFragment(SearchFragment())

            true
        }

        watchedFragmentItem.setOnMenuItemClickListener {
            replaceFragment(WatchedFragment())

            true
        }

        watchlistFragmentItem.setOnMenuItemClickListener {
            replaceFragment(WatchlistFragment())

            true
        }

    }

}
