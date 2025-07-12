// src/main/kotlin/com/movielibrary/repository/MovieRepository.kt
package com.movielibrary.repository

import com.movielibrary.models.Movie
import java.util.concurrent.ConcurrentHashMap // เพื่อความปลอดภัยของ Thread

class MovieRepository {
    private val movies = ConcurrentHashMap<String, Movie>()

    fun addMovie(movie: Movie): Movie {
        movies[movie.id] = movie
        return movie
    }

    fun getAllMovies(): List<Movie> = movies.values.toList()

    fun getMovieById(id: String): Movie? = movies[id]

    fun updateMovie(id: String, updatedMovie: Movie): Boolean {
        return if (movies.containsKey(id)) {
            movies[id] = updatedMovie.copy(id = id) // ใช้ copy เพื่อให้แน่ใจว่า ID ไม่เปลี่ยน
            true
        } else {
            false
        }
    }

    fun deleteMovie(id: String): Boolean = movies.remove(id) != null

    fun searchMovies(title: String?, director: String?): List<Movie> {
        return movies.values.filter { movie ->
            (title == null || movie.title.contains(title, ignoreCase = true)) &&
                    (director == null || movie.director.contains(director, ignoreCase = true))
        }
    }

    fun clear() { // สำหรับใช้ในการทดสอบ
        movies.clear()
    }
}