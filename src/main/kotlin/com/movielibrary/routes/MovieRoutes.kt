// src/main/kotlin/com/movielibrary/routes/MovieRoutes.kt
package com.movielibrary.routes

import com.movielibrary.models.MovieRequest
import com.movielibrary.service.MovieService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.movieRoutes(movieService: MovieService) {
    route("/movies") {
        // GET all movies
        get {
            call.respond(movieService.getAllMovies())
        }

        // POST a new movie
        post {
            val request = call.receive<MovieRequest>()
            val movie = movieService.addMovie(request.title, request.director, request.releaseYear)
            call.respond(HttpStatusCode.Created, movie)
        }

        // GET movie by ID
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            val movie = movieService.getMovieById(id)
            if (movie != null) {
                call.respond(movie)
            } else {
                call.respondText("Movie not found", status = HttpStatusCode.NotFound)
            }
        }

        // PUT update movie by ID
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            val request = call.receive<MovieRequest>()
            val updatedMovie = movieService.updateMovie(id, request.title, request.director, request.releaseYear)
            if (updatedMovie != null) {
                call.respond(updatedMovie)
            } else {
                call.respondText("Movie not found", status = HttpStatusCode.NotFound)
            }
        }

        // DELETE movie by ID
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            if (movieService.deleteMovie(id)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respondText("Movie not found", status = HttpStatusCode.NotFound)
            }
        }
    }

    // Endpoint for movies with average ratings
    get("/movies-with-ratings") {
        call.respond(movieService.getAllMoviesWithAverageRating())
    }

    // Endpoint for average rating of a single movie
    get("/movies/{id}/rating") {
        val id = call.parameters["id"] ?: return@get call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
        val (averageRating, totalReviews) = movieService.getMovieAverageRating(id)
            ?: return@get call.respondText("Movie not found", status = HttpStatusCode.NotFound)
        call.respond(com.movielibrary.models.AverageRatingResponse(averageRating, totalReviews))
    }

    // Endpoint for searching movies
    get("/movies/search") {
        val title = call.request.queryParameters["title"]
        val director = call.request.queryParameters["director"]

        if (title == null && director == null) {
            call.respondText("At least one search parameter (title or director) is required.", status = HttpStatusCode.BadRequest)
            return@get
        }

        val movies = movieService.searchMovies(title, director)
        call.respond(movies)
    }
}