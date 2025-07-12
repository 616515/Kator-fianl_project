// src/test/kotlin/com/movielibrary/repository/ReviewRepositoryTest.kt
package com.movielibrary.repository

import com.movielibrary.models.Review
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.*

class ReviewRepositoryTest {

    private lateinit var reviewRepository: ReviewRepository

    @BeforeEach
    fun setUp() {
        reviewRepository = ReviewRepository()
        reviewRepository.clear() // Clear data before each test
    }

    @Test
    fun `addReview should add a review to the repository`() {
        val review = Review("r1", "m1", "John Doe", 5, "Great movie!")
        val addedReview = reviewRepository.addReview(review)
        assertEquals(review, addedReview)
        assertEquals(review, reviewRepository.getReviewById("r1"))
    }

    @Test
    fun `getReviewById should return correct review or null if not found`() {
        val review = Review("r1", "m1", "John Doe", 5, "Great movie!")
        reviewRepository.addReview(review)
        assertEquals(review, reviewRepository.getReviewById("r1"))
        assertNull(reviewRepository.getReviewById("r2"))
    }

    @Test
    fun `getReviewsByMovieId should return all reviews for a specific movie`() {
        val review1 = Review("r1", "m1", "John", 5, "Good")
        val review2 = Review("r2", "m1", "Jane", 4, "Okay")
        val review3 = Review("r3", "m2", "Bob", 3, "Meh")
        reviewRepository.addReview(review1)
        reviewRepository.addReview(review2)
        reviewRepository.addReview(review3)

        val reviewsForM1 = reviewRepository.getReviewsByMovieId("m1")
        assertEquals(2, reviewsForM1.size)
        assertTrue(reviewsForM1.contains(review1))
        assertTrue(reviewsForM1.contains(review2))
    }

    @Test
    fun `getReviewsByMovieId should return empty list if no reviews for movie`() {
        val review1 = Review("r1", "m1", "John", 5, "Good")
        reviewRepository.addReview(review1)

        val reviewsForM2 = reviewRepository.getReviewsByMovieId("m2")
        assertTrue(reviewsForM2.isEmpty())
    }

    @Test
    fun `updateReview should update existing review`() {
        val review = Review("r1", "m1", "Old Name", 3, "Old Comment")
        reviewRepository.addReview(review)

        val updatedReview = Review("r1", "m1", "New Name", 5, "New Comment")
        val result = reviewRepository.updateReview("r1", updatedReview)
        assertTrue(result)
        assertEquals("New Name", reviewRepository.getReviewById("r1")?.reviewerName)
        assertEquals(5, reviewRepository.getReviewById("r1")?.rating)
    }

    @Test
    fun `updateReview should return false if review does not exist`() {
        val updatedReview = Review("r99", "m1", "New Name", 5, "New Comment")
        val result = reviewRepository.updateReview("r99", updatedReview)
        assertFalse(result)
    }

    @Test
    fun `deleteReview should remove review`() {
        val review = Review("r1", "m1", "John Doe", 5, "Great movie!")
        reviewRepository.addReview(review)

        val result = reviewRepository.deleteReview("r1")
        assertTrue(result)
        assertNull(reviewRepository.getReviewById("r1"))
    }

    @Test
    fun `deleteReview should return false if review does not exist`() {
        val result = reviewRepository.deleteReview("r99")
        assertFalse(result)
    }
}