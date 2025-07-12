// src/main/kotlin/com/movielibrary/service/MovieService.kt
package com.movielibrary.service

import com.movielibrary.models.Movie
import com.movielibrary.models.MovieWithAverageRating
import com.movielibrary.models.Review
import com.movielibrary.repository.MovieRepository
import com.movielibrary.repository.ReviewRepository
import java.util.UUID

class MovieService(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
) {
    fun addMovie(title: String, director: String, releaseYear: Int): Movie {
        val newId = UUID.randomUUID().toString()
        val movie = Movie(newId, title, director, releaseYear)
        return movieRepository.addMovie(movie)
    }

    fun getAllMovies(): List<Movie> = movieRepository.getAllMovies()

    fun getMovieById(id: String): Movie? = movieRepository.getMovieById(id)

    fun updateMovie(id: String, title: String, director: String, releaseYear: Int): Movie? {
        if (movieRepository.getMovieById(id) == null) {
            return null
        }
        val updatedMovie = Movie(id, title, director, releaseYear)
        return if (movieRepository.updateMovie(id, updatedMovie)) updatedMovie else null
    }

    fun deleteMovie(id: String): Boolean = movieRepository.deleteMovie(id)

    fun searchMovies(title: String?, director: String?): List<Movie> {
        if (title == null && director == null) {
            return emptyList() // หรือ throw IllegalArgumentException("At least one search parameter (title or director) is required.")
        }
        return movieRepository.searchMovies(title, director)
    }

    fun getAllMoviesWithAverageRating(): List<MovieWithAverageRating> {
        return movieRepository.getAllMovies().map { movie ->
            val reviews = reviewRepository.getReviewsByMovieId(movie.id)
            val averageRating = if (reviews.isNotEmpty()) {
                reviews.map(Review::rating).average()
            } else {
                0.0
            }
            MovieWithAverageRating(
                id = movie.id,
                title = movie.title,
                director = movie.director,
                releaseYear = movie.releaseYear,
                averageRating = averageRating,
                totalReviews = reviews.size
            )
        }
    }

    fun getMovieAverageRating(movieId: String): Pair<Double, Int>? {
        if (movieRepository.getMovieById(movieId) == null) {
            return null
        }
        val reviews = reviewRepository.getReviewsByMovieId(movieId)
        val averageRating = if (reviews.isNotEmpty()) {
            reviews.map(Review::rating).average()
        } else {
            0.0
        }
        return Pair(averageRating, reviews.size)
    }
}