package com.enestekin.routes


import com.enestekin.data.requests.CreatePostRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPostRoute(
    postService: PostService,
    userService: UserService
){

    authenticate {
        post("/api/post/create"){
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post

            }
/*
when we attached a claim  which is email.user logs in . email saves in token.
 which user can't modifier.here we get that email from token
 *
 **/
            val email = call.principal<JWTPrincipal>()?.getClaim("email",String::class)
            val isEmailByUser =userService.doesEmailBelongToUserId(
                email =  email ?: "",
                userId = request.userId
            )

            if (!isEmailByUser){
                call.respond(HttpStatusCode.Unauthorized,"You are not who you say you are.")
                return@post
            }


            val didUserExist = postService.createPostIfUserExists(request)

            if (!didUserExist){
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                    )
                )
            }


        }
    }



}