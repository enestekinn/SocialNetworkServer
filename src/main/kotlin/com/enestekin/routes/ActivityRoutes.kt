package com.enestekin.routes

import com.enestekin.service.ActivityService
import com.enestekin.util.Constants.DEFAULT_POST_PAGE_SIZE
import com.enestekin.util.QueryParams.PARAM_PAGE
import com.enestekin.util.QueryParams.PARAM_PAGE_SIZE
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getActivities(
    activityService: ActivityService,
) {
    authenticate {
        get("/api/activity/get") {
            val page = call.parameters[PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[PARAM_PAGE_SIZE]?.toIntOrNull() ?:
            DEFAULT_POST_PAGE_SIZE

            val activities = activityService.getActivitiesForUser(call.userId, page, pageSize)
            call.respond(
                HttpStatusCode.OK,
                activities
            )
        }
    }
}
