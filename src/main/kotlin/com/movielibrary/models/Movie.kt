// src/main/kotlin/com/movielibrary/models/Movie.kt
package com.movielibrary.models

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: String,
    val title: String,
    val director: String,
    val releaseYear: Int
)

@Serializable
data class MovieRequest( // สำหรับรับ Request Body ตอนสร้าง/แก้ไข Movie
    val title: String,
    val director: String,
    val releaseYear: Int
)

@Serializable
data class MovieWithAverageRating( // สำหรับ Endpoint ที่แสดงคะแนนเฉลี่ย
    val id: String,
    val title: String,
    val director: String,
    val releaseYear: Int,
    val averageRating: Double,
    val totalReviews: Int
)