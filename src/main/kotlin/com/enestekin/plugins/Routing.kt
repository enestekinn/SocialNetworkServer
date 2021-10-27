package com.enestekin.plugins

import com.enestekin.repository.UserRepository
import com.enestekin.routes.createUserRoute
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userRepository: UserRepository by inject()


    routing {

        createUserRoute(userRepository)


    }



}
