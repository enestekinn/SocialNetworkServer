package com.enestekin.service

import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.requests.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest): Boolean{
        return followRepository.followUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }

    suspend fun unfollowUserIfExists (request: FollowUpdateRequest): Boolean {
        return followRepository.unfollowUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }
}