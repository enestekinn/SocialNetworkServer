package com.enestekin.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {
    

    routing {
        get("/") {  // create a respond
                call.respondText("Hello World!")
            }
        // Static plugin. Try to access `/static/index.html`
        static("/static") { // provide file like image
            resources("static")
        }
    }
}
