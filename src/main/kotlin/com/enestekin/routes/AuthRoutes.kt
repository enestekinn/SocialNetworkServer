package com.enestekin.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.enestekin.data.requests.CreateAccountRequest
import com.enestekin.data.requests.LoginRequest
import com.enestekin.data.responses.AuthResponse
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.createUser(userService: UserService) {



    route("/api/user/create") {


        post {

            val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if(userService.doesUserWithEmailExist(request.email)) {
                call.respond(
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_ALREADY_EXISTS
                    )
                )
                return@post
            }

            when(userService.validateCreateAccountRequest(request)){
                is UserService.ValidationEvent.ErrorFieldEmpty -> {
                    call.respond(
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.FIELDS_BLANK
                        )
                    )
                }
                is UserService.ValidationEvent.Success -> {
                    userService.createUser(request)
                    call.respond(
                        BasicApiResponse<Unit>(successful = true)
                    )
                }

            }



        }
    }
}

fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {


    //post("/api/user/login"){ }

    route("/api/user/login") {


        post {

            val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (request.email.isBlank() && request.password.isBlank()){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val user = userService.getUserByEmail(request.email) ?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.INVALID_CREDENTIALS
                    )
                )
                return@post
            }
            val isCorrectPassword = userService.isValidPassword(
                enteredPassword = request.password,
                actualPassword = user.password
            )

            if (isCorrectPassword){
                // when user is logged  in , produce token  and attached it to respond
                val expiresIn = 1000L * 60L * 60L * 24L * 365L // one year
                val token = JWT.create()
                    .withClaim("userId",user.id)
                    .withIssuer(jwtIssuer)
                    .withExpiresAt(Date(System.currentTimeMillis() + expiresIn)) // token expires in a year
                    .withAudience(jwtAudience)
                    .sign(Algorithm.HMAC256(jwtSecret))
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = AuthResponse(
                            userId = user.id,
                            token = token)
                    )
                )
            }else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.INVALID_CREDENTIALS,
                    )
                )
            }


        }
    }
}

fun Route.authenticate() {

    authenticate {

        get("/api/user/authenticate"){

        call.respond(HttpStatusCode.OK)
        }
    }

}