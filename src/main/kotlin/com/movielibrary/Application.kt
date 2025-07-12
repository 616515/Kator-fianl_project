// src/main/kotlin/com/movielibrary/Application.kt
package com.movielibrary

import com.movielibrary.plugins.configureRouting
import com.movielibrary.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    configureStatusPages()
    configureRouting()
}