package com.enestekin.routes

import com.enestekin.data.requests.LikeUpdateRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.service.LikeService
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.likeParent(
    likeService: LikeService,
    userService: UserService
){
    authenticate {
        post("/api/like"){
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){
                val likeSuccessful  = likeService.likeParent(request.userId, request.parentId)
                if (likeSuccessful){
                    call.respond(HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                    )
                }else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
            }
        }
    }
}

fun Route.unlikeParent(
    likeService: LikeService,
    userService: UserService
){
    authenticate {
        delete("/api/unlike"){
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){
                val unlikeSuccessful  = likeService.unlikeParent(request.userId, request.parentId)
                if (unlikeSuccessful){
                    call.respond(HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true
                        )
                    )
                }else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
            }
        }
    }
}