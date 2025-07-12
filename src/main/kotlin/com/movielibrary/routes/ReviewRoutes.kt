// src/main/kotlin/com/movielibrary/routes/ReviewRoutes.kt
package com.movielibrary.routes

import com.movielibrary.models.ReviewRequest
import com.movielibrary.service.ReviewService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.reviewRoutes(reviewService: ReviewService) {
    route("/movies/{movieId}/reviews") {
        // POST a new review for a specific movie
        post {
            val movieId = call.parameters["movieId"] ?: return@post call.respondText("Missing Movie ID", status = HttpStatusCode.BadRequest)
            val request = call.receive<ReviewRequest>()
            val review = reviewService.addReview(movieId, request.reviewerName, request.rating, request.comment)
            if (review != null) {
                call.respond(HttpStatusCode.Created, review)
            } else {
                call.respondText("Could not create review. Invalid movie ID or rating (1-5).", status = HttpStatusCode.BadRequest)
            }
        }

        // GET all reviews for a specific movie
        get {
            val movieId = call.parameters["movieId"] ?: return@get call.respondText("Missing Movie ID", status = HttpStatusCode.BadRequest)
            val reviews = reviewService.getReviewsByMovieId(movieId)
            call.respond(reviews)
        }
    }

    // Routes for individual reviews (outside movie-specific scope for PUT/DELETE by review ID)
    route("/reviews") {
        // GET review by ID
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            val review = reviewService.getReviewById(id)
            if (review != null) {
                call.respond(review)
            } else {
                call.respondText("Review not found", status = HttpStatusCode.NotFound)
            }
        }

        // PUT update review by ID
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            val request = call.receive<ReviewRequest>()
            val updatedReview = reviewService.updateReview(id, request.reviewerName, request.rating, request.comment)
            if (updatedReview != null) {
                call.respond(updatedReview)
            } else {
                call.respondText("Could not update review. Review not found or invalid rating (1-5).", status = HttpStatusCode.NotFound)
            }
        }

        // DELETE review by ID
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            if (reviewService.deleteReview(id)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respondText("Review not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}