// src/main/kotlin/com/movielibrary/plugins/StatusPages.kt
package com.movielibrary.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Page not found", status = status)
        }
        status(HttpStatusCode.BadRequest) { call, status ->
            call.respondText(text = "400: Bad Request", status = status)
        }
    }
}