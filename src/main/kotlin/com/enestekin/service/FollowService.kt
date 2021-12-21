package com.enestekin.service

import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.requests.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean{
        return followRepository.followUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }

    suspend fun unfollowUserIfExists (followedUserId: String ,followingUserId: String): Boolean {
        return followRepository.unfollowUserIfExists(
            followingUserId,
            followedUserId
        )
    }
}