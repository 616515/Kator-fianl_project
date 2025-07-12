// src/test/kotlin/com/movielibrary/repository/MovieRepositoryTest.kt
package com.movielibrary.repository

import com.movielibrary.models.Movie
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class MovieRepositoryTest {

    private lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setUp() {
        movieRepository = MovieRepository()
        movieRepository.clear() // Clear data before each test
    }

    @Test
    fun `addMovie should add a movie to the repository`() {
        val movie = Movie("1", "Test Movie", "Test Director", 2023)
        val addedMovie = movieRepository.addMovie(movie)
        assertEquals(movie, addedMovie)
        assertEquals(1, movieRepository.getAllMovies().size)
        assertEquals(movie, movieRepository.getMovieById("1"))
    }

    @Test
    fun `getAllMovies should return all movies`() {
        val movie1 = Movie("1", "Movie 1", "Director 1", 2020)
        val movie2 = Movie("2", "Movie 2", "Director 2", 2021)
        movieRepository.addMovie(movie1)
        movieRepository.addMovie(movie2)

        val allMovies = movieRepository.getAllMovies()
        assertEquals(2, allMovies.size)
        assertTrue(allMovies.contains(movie1))
        assertTrue(allMovies.contains(movie2))
    }

    @Test
    fun `getMovieById should return correct movie or null if not found`() {
        val movie = Movie("1", "Test Movie", "Test Director", 2023)
        movieRepository.addMovie(movie)

        assertEquals(movie, movieRepository.getMovieById("1"))
        assertNull(movieRepository.getMovieById("2"))
    }

    @Test
    fun `updateMovie should update existing movie`() {
        val movie = Movie("1", "Old Title", "Old Director", 2023)
        movieRepository.addMovie(movie)

        val updatedMovie = Movie("1", "New Title", "New Director", 2024)
        val result = movieRepository.updateMovie("1", updatedMovie)
        assertTrue(result)
        assertEquals("New Title", movieRepository.getMovieById("1")?.title)
    }

    @Test
    fun `updateMovie should return false if movie does not exist`() {
        val updatedMovie = Movie("99", "New Title", "New Director", 2024)
        val result = movieRepository.updateMovie("99", updatedMovie)
        assertFalse(result)
    }

    @Test
    fun `deleteMovie should remove movie`() {
        val movie = Movie("1", "Test Movie", "Test Director", 2023)
        movieRepository.addMovie(movie)

        val result = movieRepository.deleteMovie("1")
        assertTrue(result)
        assertNull(movieRepository.getMovieById("1"))
        assertEquals(0, movieRepository.getAllMovies().size)
    }

    @Test
    fun `deleteMovie should return false if movie does not exist`() {
        val result = movieRepository.deleteMovie("99")
        assertFalse(result)
    }

    @Test
    fun `searchMovies should return movies by title`() {
        val movie1 = Movie("1", "The Matrix", "Wachowskis", 1999)
        val movie2 = Movie("2", "Matrix Reloaded", "Wachowskis", 2003)
        val movie3 = Movie("3", "Inception", "Nolan", 2010)
        movieRepository.addMovie(movie1)
        movieRepository.addMovie(movie2)
        movieRepository.addMovie(movie3)

        val results = movieRepository.searchMovies("matrix", null)
        assertEquals(2, results.size)
        assertTrue(results.contains(movie1))
        assertTrue(results.contains(movie2))
    }

    @Test
    fun `searchMovies should return movies by director`() {
        val movie1 = Movie("1", "The Matrix", "Wachowskis", 1999)
        val movie2 = Movie("2", "Matrix Reloaded", "Wachowskis", 2003)
        val movie3 = Movie("3", "Inception", "Nolan", 2010)
        movieRepository.addMovie(movie1)
        movieRepository.addMovie(movie2)
        movieRepository.addMovie(movie3)

        val results = movieRepository.searchMovies(null, "Wachowskis")
        assertEquals(2, results.size)
        assertTrue(results.contains(movie1))
        assertTrue(results.contains(movie2))
    }

    @Test
    fun `searchMovies should return movies by title and director`() {
        val movie1 = Movie("1", "The Matrix", "Wachowskis", 1999)
        val movie2 = Movie("2", "Matrix Reloaded", "Wachowskis", 2003)
        movieRepository.addMovie(movie1)
        movieRepository.addMovie(movie2)

        val results = movieRepository.searchMovies("Matrix", "Wachowskis")
        assertEquals(2, results.size)
    }

    @Test
    fun `searchMovies should return empty list if no match`() {
        val movie1 = Movie("1", "The Matrix", "Wachowskis", 1999)
        movieRepository.addMovie(movie1)

        val results = movieRepository.searchMovies("NonExistent", "NonExistent")
        assertTrue(results.isEmpty())
    }
}