package com.enestekin.data.repository.follow

interface FollowRepository  {

    suspend fun followUserIfExists(
        followingUserEmail: String,
        followedUserEmail: String
    ): Boolean


    suspend fun unfollowUserIfExists(
        followingUserId: String,
        followedUserId: String,
    ): Boolean
}