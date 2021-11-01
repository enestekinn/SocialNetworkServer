package com.enestekin.routes

import com.enestekin.data.models.Activity
import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.requests.FollowUpdateRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.data.util.ActivityType
import com.enestekin.service.ActivityService
import com.enestekin.service.FollowService
import com.enestekin.util.ApiResponseMessages.USER_NOT_FOUND
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.followUser(
    followService: FollowService,
    activityService: ActivityService
) {

    authenticate {


        post("/api/following/follow") {

            val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post

            }


            val didUserExist = followService.followUserIfExists(request, call.userId)
            if (didUserExist) {
                activityService.createActivity(
                    Activity(
                        timestamp = System.currentTimeMillis(),
                        byUserId =call.userId,
                        toUserId = request.followedUserId,
                        type = ActivityType.FollowedUser.type,
                        parentId = ""
                    )
                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = USER_NOT_FOUND
                    )
                )
            }

        }

    }
}

fun Route.unfollowUser(followService: FollowService) {
    delete("/api/following/unfollow") {

        val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete

        }

        val didUserExist = followService.unfollowUserIfExists(request, call.userId)
        if (didUserExist) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = USER_NOT_FOUND
                )
            )
        }
    }
}