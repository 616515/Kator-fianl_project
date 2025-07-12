// src/main/kotlin/com/movielibrary/plugins/Routing.kt
package com.movielibrary.plugins

import com.movielibrary.repository.MovieRepository
import com.movielibrary.repository.ReviewRepository
import com.movielibrary.routes.movieRoutes
import com.movielibrary.routes.reviewRoutes
import com.movielibrary.service.MovieService
import com.movielibrary.service.ReviewService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Instantiate Repositories
    val movieRepository = MovieRepository()
    val reviewRepository = ReviewRepository()

    // Instantiate Services, injecting repositories
    val movieService = MovieService(movieRepository, reviewRepository)
    val reviewService = ReviewService(reviewRepository, movieRepository)

    routing {
        // Pass services to route definition functions
        movieRoutes(movieService)
        reviewRoutes(reviewService)
    }
}