// src/main/kotlin/com/movielibrary/models/Review.kt
package com.movielibrary.models

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String,
    val movieId: String,
    val reviewerName: String,
    val rating: Int, // 1-5
    val comment: String? = null
)

@Serializable
data class ReviewRequest( // สำหรับรับ Request Body ตอนสร้าง/แก้ไข Review
    val reviewerName: String,
    val rating: Int,
    val comment: String? = null
)

@Serializable
data class AverageRatingResponse( // สำหรับ Endpoint ที่แสดงคะแนนเฉลี่ยของ Movie เดียว
    val averageRating: Double,
    val totalReviews: Int
)