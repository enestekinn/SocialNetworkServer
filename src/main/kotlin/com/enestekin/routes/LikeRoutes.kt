package com.enestekin.routes

import com.enestekin.data.requests.LikeUpdateRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.data.util.ParentType
import com.enestekin.service.ActivityService
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
    activityService: ActivityService
){
    authenticate {
        post("/api/like"){
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userId = call.userId
                val likeSuccessful  = likeService.likeParent(call.userId, request.parentId,request.parentType)
                if (likeSuccessful){
                    activityService.addLikeActivity(
                        byUserId = userId,
                        parentType = ParentType.fromType(request.parentType),
                        parentId = request.parentId

                    )
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

                val unlikeSuccessful  = likeService.unlikeParent(call.userId, request.parentId)
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