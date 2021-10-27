package com.enestekin.plugins

import com.enestekin.data.repository.UserRepository
import com.enestekin.routes.createUserRoute
import com.enestekin.routes.loginUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userRepository: UserRepository by inject()


    routing {

        createUserRoute(userRepository)
        loginUser(userRepository)


    }



}
