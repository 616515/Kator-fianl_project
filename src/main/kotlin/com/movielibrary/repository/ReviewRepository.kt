// src/main/kotlin/com/movielibrary/repository/ReviewRepository.kt
package com.movielibrary.repository

import com.movielibrary.models.Review
import java.util.concurrent.ConcurrentHashMap

class ReviewRepository {
    private val reviews = ConcurrentHashMap<String, Review>()

    fun addReview(review: Review): Review {
        reviews[review.id] = review
        return review
    }

    fun getReviewById(id: String): Review? = reviews[id]

    fun getReviewsByMovieId(movieId: String): List<Review> {
        return reviews.values.filter { it.movieId == movieId }
    }

    fun updateReview(id: String, updatedReview: Review): Boolean {
        return if (reviews.containsKey(id)) {
            reviews[id] = updatedReview.copy(id = id)
            true
        } else {
            false
        }
    }

    fun deleteReview(id: String): Boolean = reviews.remove(id) != null

    fun clear() { // สำหรับใช้ในการทดสอบ
        reviews.clear()
    }
}