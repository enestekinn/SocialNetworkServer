package com.enestekin.data.repository.activity

import com.enestekin.data.models.Activity
import com.enestekin.util.Constants.DEFAULT_ACTIVITY_PAGE_SIZE

interface ActivityRepository {

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<Activity>

    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean
}