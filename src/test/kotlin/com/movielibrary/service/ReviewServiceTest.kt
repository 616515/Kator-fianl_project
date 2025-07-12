// src/test/kotlin/com/movielibrary/service/ReviewServiceTest.kt
package com.movielibrary.service

import com.movielibrary.models.Movie
import com.movielibrary.models.Review
import com.movielibrary.repository.MovieRepository
import com.movielibrary.repository.ReviewRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class ReviewServiceTest {

    private lateinit var reviewService: ReviewService
    private val reviewRepository: ReviewRepository = mockk()
    private val movieRepository: MovieRepository = mockk()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        reviewService = ReviewService(reviewRepository, movieRepository)
    }

    @Test
    fun `addReview should return added review if valid and movie exists`() {
        val movieId = "m1"
        val review = Review("any_id", movieId, "Reviewer", 4, "Comment")
        val movie = Movie(movieId, "Title", "Director", 2023)

        every { movieRepository.getMovieById(movieId) } returns movie
        every { reviewRepository.addReview(any()) } returns review

        val result = reviewService.addReview(movieId, "Reviewer", 4, "Comment")
        assertNotNull(result)
        assertEquals(movieId, result.movieId)
        assertEquals(4, result.rating)
        verify(exactly = 1) { movieRepository.getMovieById(movieId) }
        verify(exactly = 1) { reviewRepository.addReview(any()) }
    }

    @Test
    fun `addReview should return null if rating is invalid`() {
        val result = reviewService.addReview("m1", "Reviewer", 6, "Comment")
        assertNull(result)
        verify(exactly = 0) { movieRepository.getMovieById(any()) } // Should not even check movie
        verify(exactly = 0) { reviewRepository.addReview(any()) }
    }

    @Test
    fun `addReview should return null if movie does not exist`() {
        val movieId = "non_existent"
        every { movieRepository.getMovieById(movieId) } returns null

        val result = reviewService.addReview(movieId, "Reviewer", 4, "Comment")
        assertNull(result)
        verify(exactly = 1) { movieRepository.getMovieById(movieId) }
        verify(exactly = 0) { reviewRepository.addReview(any()) }
    }

    @Test
    fun `getReviewById should return review if found`() {
        val review = Review("r1", "m1", "Reviewer", 4)
        every { reviewRepository.getReviewById("r1") } returns review

        val result = reviewService.getReviewById("r1")
        assertEquals(review, result)
        verify(exactly = 1) { reviewRepository.getReviewById("r1") }
    }

    @Test
    fun `getReviewById should return null if not found`() {
        every { reviewRepository.getReviewById("non_existent") } returns null

        val result = reviewService.getReviewById("non_existent")
        assertNull(result)
        verify(exactly = 1) { reviewRepository.getReviewById("non_existent") }
    }

    @Test
    fun `getReviewsByMovieId should return reviews for given movie ID`() {
        val reviews = listOf(Review("r1", "m1", "Reviewer", 4))
        every { reviewRepository.getReviewsByMovieId("m1") } returns reviews

        val result = reviewService.getReviewsByMovieId("m1")
        assertEquals(reviews, result)
        verify(exactly = 1) { reviewRepository.getReviewsByMovieId("m1") }
    }

    @Test
    fun `updateReview should return updated review if exists and valid rating`() {
        val existingReview = Review("r1", "m1", "Old Name", 3, "Old Comment")
        val updatedReview = existingReview.copy(reviewerName = "New Name", rating = 5, comment = "New Comment")

        every { reviewRepository.getReviewById("r1") } returns existingReview
        every { reviewRepository.updateReview("r1", any()) } returns true

        val result = reviewService.updateReview("r1", "New Name", 5, "New Comment")
        assertEquals(updatedReview, result)
        verify(exactly = 1) { reviewRepository.getReviewById("r1") }
        verify(exactly = 1) { reviewRepository.updateReview("r1", updatedReview) }
    }

    @Test
    fun `updateReview should return null if review not found`() {
        every { reviewRepository.getReviewById("non_existent") } returns null

        val result = reviewService.updateReview("non_existent", "New Name", 5, "New Comment")
        assertNull(result)
        verify(exactly = 1) { reviewRepository.getReviewById("non_existent") }
        verify(exactly = 0) { reviewRepository.updateReview(any(), any()) }
    }

    @Test
    fun `updateReview should return null if rating is invalid`() {
        val existingReview = Review("r1", "m1", "Old Name", 3, "Old Comment")
        every { reviewRepository.getReviewById("r1") } returns existingReview

        val result = reviewService.updateReview("r1", "New Name", 0, "New Comment")
        assertNull(result)
        verify(exactly = 1) { reviewRepository.getReviewById("r1") } // Check for existence first
        verify(exactly = 0) { reviewRepository.updateReview(any(), any()) }
    }

    @Test
    fun `deleteReview should return true if review deleted`() {
        every { reviewRepository.deleteReview("r1") } returns true

        val result = reviewService.deleteReview("r1")
        assertTrue(result)
        verify(exactly = 1) { reviewRepository.deleteReview("r1") }
    }

    @Test
    fun `deleteReview should return false if review not found`() {
        every { reviewRepository.deleteReview("non_existent") } returns false

        val result = reviewService.deleteReview("non_existent")
        assertFalse(result)
        verify(exactly = 1) { reviewRepository.deleteReview("non_existent") }
    }
}