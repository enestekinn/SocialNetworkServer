package com.enestekin.data.repository.follow

import com.enestekin.data.models.Following

interface FollowRepository  {

    suspend fun followUserIfExists(
        followingUserEmail: String,
        followedUserEmail: String
    ): Boolean


    suspend fun unfollowUserIfExists(
        followingUserId: String,
        followedUserId: String,
    ): Boolean

    suspend fun getFollowsByUser(userId: String): List<Following>

    suspend fun doesUserFollow(followingUserId: String, followedUserId: String): Boolean
}