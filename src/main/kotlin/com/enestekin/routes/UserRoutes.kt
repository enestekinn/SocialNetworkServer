package com.enestekin.routes

import com.enestekin.repository.UserRepository
import com.enestekin.data.models.User
import com.enestekin.data.requests.CreateAccountRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.util.ApiResponseMessages.FIELDS_BLANK
import com.enestekin.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.createUserRoute(userRepository: UserRepository) {



    route("/api/user/create") {


        post {



            val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            println("${request.email}  ${request.username}")
            val userExists = userRepository.getUserByEmail(request.email) != null
            if(userExists) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = USER_ALREADY_EXISTS
                    )
                )
                return@post
            }
            if(request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = FIELDS_BLANK
                    )
                )
                return@post
            }
            userRepository.createUser(
                User(
                    email = request.email,
                    username = request.username,
                    password = request.password,
                    profileImageUrl = "",
                    bio = "",
                    gitHubUrl = "null",
                    instagramUrl = "null",
                    linkedInUrl = "null"
                )
            )

            call.respond(
                BasicApiResponse(successful = true)
            )
        }
    }
}