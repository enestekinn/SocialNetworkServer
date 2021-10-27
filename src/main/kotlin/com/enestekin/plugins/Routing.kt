package com.enestekin.plugins

import com.enestekin.routes.userRoutes
import io.ktor.routing.*
import io.ktor.application.*



fun Application.configureRouting() {


    routing {

        userRoutes()


    }



}
