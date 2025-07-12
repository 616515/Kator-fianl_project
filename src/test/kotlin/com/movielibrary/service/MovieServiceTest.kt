// src/test/kotlin/com/movielibrary/service/MovieServiceTest.kt
package com.movielibrary.service

import com.movielibrary.models.Movie
import com.movielibrary.models.Review
import com.movielibrary.repository.MovieRepository
import com.movielibrary.repository.ReviewRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class MovieServiceTest {

    private lateinit var movieService: MovieService
    private val movieRepository: MovieRepository = mockk()
    private val reviewRepository: ReviewRepository = mockk()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        movieService = MovieService(movieRepository, reviewRepository)
    }

    @Test
    fun `addMovie should return the added movie`() {
        val movie = Movie("any_id", "Test Title", "Test Director", 2023)
        every { movieRepository.addMovie(any()) } returns movie

        val result = movieService.addMovie("Test Title", "Test Director", 2023)
        assertNotNull(result.id) // Check if ID is generated
        assertEquals("Test Title", result.title)
        verify(exactly = 1) { movieRepository.addMovie(any()) }
    }

    @Test
    fun `getAllMovies should return all movies from repository`() {
        val movies = listOf(Movie("1", "Movie 1", "Dir 1", 2000))
        every { movieRepository.getAllMovies() } returns movies

        val result = movieService.getAllMovies()
        assertEquals(movies, result)
        verify(exactly = 1) { movieRepository.getAllMovies() }
    }

    @Test
    fun `getMovieById should return movie if found`() {
        val movie = Movie("1", "Movie 1", "Dir 1", 2000)
        every { movieRepository.getMovieById("1") } returns movie

        val result = movieService.getMovieById("1")
        assertEquals(movie, result)
        verify(exactly = 1) { movieRepository.getMovieById("1") }
    }

    @Test
    fun `getMovieById should return null if not found`() {
        every { movieRepository.getMovieById("non_existent") } returns null

        val result = movieService.getMovieById("non_existent")
        assertNull(result)
        verify(exactly = 1) { movieRepository.getMovieById("non_existent") }
    }

    @Test
    fun `updateMovie should return updated movie if exists`() {
        val existingMovie = Movie("1", "Old Title", "Old Dir", 2000)
        val updatedMovie = Movie("1", "New Title", "New Dir", 2001)

        every { movieRepository.getMovieById("1") } returns existingMovie
        every { movieRepository.updateMovie("1", any()) } returns true

        val result = movieService.updateMovie("1", "New Title", "New Dir", 2001)
        assertEquals(updatedMovie, result)
        verify(exactly = 1) { movieRepository.getMovieById("1") }
        verify(exactly = 1) { movieRepository.updateMovie("1", updatedMovie) }
    }

    @Test
    fun `updateMovie should return null if movie does not exist`() {
        every { movieRepository.getMovieById("non_existent") } returns null

        val result = movieService.updateMovie("non_existent", "New Title", "New Dir", 2001)
        assertNull(result)
        verify(exactly = 1) { movieRepository.getMovieById("non_existent") }
        verify(exactly = 0) { movieRepository.updateMovie(any(), any()) }
    }

    @Test
    fun `deleteMovie should return true if movie deleted`() {
        every { movieRepository.deleteMovie("1") } returns true

        val result = movieService.deleteMovie("1")
        assertTrue(result)
        verify(exactly = 1) { movieRepository.deleteMovie("1") }
    }

    @Test
    fun `deleteMovie should return false if movie not found`() {
        every { movieRepository.deleteMovie("non_existent") } returns false

        val result = movieService.deleteMovie("non_existent")
        assertFalse(result)
        verify(exactly = 1) { movieRepository.deleteMovie("non_existent") }
    }

    @Test
    fun `searchMovies should return movies by title`() {
        val movies = listOf(Movie("1", "Matrix", "Dir A", 2000))
        every { movieRepository.searchMovies("Matrix", null) } returns movies

        val result = movieService.searchMovies("Matrix", null)
        assertEquals(movies, result)
        verify(exactly = 1) { movieRepository.searchMovies("Matrix", null) }
    }

    @Test
    fun `searchMovies should return movies by director`() {
        val movies = listOf(Movie("1", "Matrix", "Dir A", 2000))
        every { movieRepository.searchMovies(null, "Dir A") } returns movies

        val result = movieService.searchMovies(null, "Dir A")
        assertEquals(movies, result)
        verify(exactly = 1) { movieRepository.searchMovies(null, "Dir A") }
    }

    @Test
    fun `searchMovies should return empty list if no parameters provided`() {
        val result = movieService.searchMovies(null, null)
        assertTrue(result.isEmpty())
        verify(exactly = 0) { movieRepository.searchMovies(any(), any()) } // Should not call repository
    }

    @Test
    fun `getAllMoviesWithAverageRating should return movies with correct average ratings`() {
        val movie1 = Movie("m1", "Movie 1", "Dir 1", 2000)
        val movie2 = Movie("m2", "Movie 2", "Dir 2", 2001)
        val reviews1 = listOf(Review("r1", "m1", "A", 5), Review("r2", "m1", "B", 3)) // Avg 4.0
        val reviews2 = listOf(Review("r3", "m2", "C", 2)) // Avg 2.0

        every { movieRepository.getAllMovies() } returns listOf(movie1, movie2)
        every { reviewRepository.getReviewsByMovieId("m1") } returns reviews1
        every { reviewRepository.getReviewsByMovieId("m2") } returns reviews2

        val result = movieService.getAllMoviesWithAverageRating()
        assertEquals(2, result.size)
        assertEquals("m1", result[0].id)
        assertEquals(4.0, result[0].averageRating)
        assertEquals(2, result[0].totalReviews)

        assertEquals("m2", result[1].id)
        assertEquals(2.0, result[1].averageRating)
        assertEquals(1, result[1].totalReviews)
        verify(exactly = 1) { movieRepository.getAllMovies() }
        verify(exactly = 1) { reviewRepository.getReviewsByMovieId("m1") }
        verify(exactly = 1) { reviewRepository.getReviewsByMovieId("m2") }
    }

    @Test
    fun `getAllMoviesWithAverageRating should return 0 average for movies with no reviews`() {
        val movie1 = Movie("m1", "Movie 1", "Dir 1", 2000)

        every { movieRepository.getAllMovies() } returns listOf(movie1)
        every { reviewRepository.getReviewsByMovieId("m1") } returns emptyList()

        val result = movieService.getAllMoviesWithAverageRating()
        assertEquals(1, result.size)
        assertEquals("m1", result[0].id)
        assertEquals(0.0, result[0].averageRating)
        assertEquals(0, result[0].totalReviews)
    }

    @Test
    fun `getMovieAverageRating should return correct average and total reviews`() {
        val movie = Movie("m1", "Movie 1", "Dir 1", 2000)
        val reviews = listOf(Review("r1", "m1", "A", 5), Review("r2", "m1", "B", 3)) // Avg 4.0

        every { movieRepository.getMovieById("m1") } returns movie
        every { reviewRepository.getReviewsByMovieId("m1") } returns reviews

        val result = movieService.getMovieAverageRating("m1")
        assertNotNull(result)
        assertEquals(4.0, result.first)
        assertEquals(2, result.second)
    }

    @Test
    fun `getMovieAverageRating should return 0 average and 0 total if no reviews`() {
        val movie = Movie("m1", "Movie 1", "Dir 1", 2000)
        every { movieRepository.getMovieById("m1") } returns movie
        every { reviewRepository.getReviewsByMovieId("m1") } returns emptyList()

        val result = movieService.getMovieAverageRating("m1")
        assertNotNull(result)
        assertEquals(0.0, result.first)
        assertEquals(0, result.second)
    }

    @Test
    fun `getMovieAverageRating should return null if movie not found`() {
        every { movieRepository.getMovieById("non_existent") } returns null
        val result = movieService.getMovieAverageRating("non_existent")
        assertNull(result)
    }
}