package com.sychev.moviemanager.data.response

data class MovieStatus(
    val watched: String = "watched",
    val watchlist: String = "watchlist",
    val undefined: Nothing? = null
) {
}