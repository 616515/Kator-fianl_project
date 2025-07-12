// src/main/kotlin/com/movielibrary/service/ReviewService.kt
package com.movielibrary.service

import com.movielibrary.models.Review
import com.movielibrary.repository.MovieRepository
import com.movielibrary.repository.ReviewRepository
import java.util.UUID

class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val movieRepository: MovieRepository // เพื่อตรวจสอบว่า movieId มีอยู่จริง
) {
    fun addReview(movieId: String, reviewerName: String, rating: Int, comment: String?): Review? {
        if (rating !in 1..5) {
            return null // Invalid rating
        }
        if (movieRepository.getMovieById(movieId) == null) {
            return null // Movie not found
        }
        val newId = UUID.randomUUID().toString()
        val review = Review(newId, movieId, reviewerName, rating, comment)
        return reviewRepository.addReview(review)
    }

    fun getReviewById(id: String): Review? = reviewRepository.getReviewById(id)

    fun getReviewsByMovieId(movieId: String): List<Review> = reviewRepository.getReviewsByMovieId(movieId)

    fun updateReview(id: String, reviewerName: String, rating: Int, comment: String?): Review? {
        if (rating !in 1..5) {
            return null // Invalid rating
        }
        val existingReview = reviewRepository.getReviewById(id) ?: return null
        val updatedReview = existingReview.copy(reviewerName = reviewerName, rating = rating, comment = comment)
        return if (reviewRepository.updateReview(id, updatedReview)) updatedReview else null
    }

    fun deleteReview(id: String): Boolean = reviewRepository.deleteReview(id)
}