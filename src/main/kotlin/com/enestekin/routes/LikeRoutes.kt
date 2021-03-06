package com.enestekin.routes

import com.enestekin.data.requests.LikeUpdateRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.data.util.ParentType
import com.enestekin.service.ActivityService
import com.enestekin.service.LikeService
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import com.enestekin.util.QueryParams
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
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                    )
                }else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BasicApiResponse<Unit>(
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

            val parentId = call.parameters[QueryParams.PARENT_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete

            }
            val parentType = call.parameters[QueryParams.PARAM_PARENT_TYPE]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete

            }


                val unlikeSuccessful  = likeService.unlikeParent(call.userId, parentId,parentType)
                if (unlikeSuccessful){
                    call.respond(HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                }else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }
            }
        }
    }

fun Route.getLikesForParent(likeService: LikeService){

    authenticate {
        get("/api/like/parent") {
            val parentId = call.parameters[QueryParams.PARENT_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val usersWhoLikedParent = likeService.getUsersWhoLikedParent(
                parentId =  parentId,
                call.userId
            )
            call.respond(
                HttpStatusCode.OK,
                usersWhoLikedParent
            )
        }
    }
}
