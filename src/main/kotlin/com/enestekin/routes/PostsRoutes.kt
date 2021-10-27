package com.enestekin.routes

import com.enestekin.data.models.Post
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.requests.CreatePostRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPostRoute(postRepository: PostRepository){
    post("/api/post/create"){
        val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post

        }


        val didUserExist = postRepository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
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