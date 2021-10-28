package com.enestekin.plugins

import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.routes.*
import com.enestekin.service.FollowService
import com.enestekin.service.UserService
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userRepository: UserRepository by inject()
    val userService: UserService by inject()
    val followService: FollowService by inject()

val followRepository: FollowRepository by inject()
val postRepository: PostRepository by inject()

    routing {

        // User routes
        createUserRoute(userService)
        loginUser(userRepository)

        // Following routes
        followUser(followService)
        unfollowUser(followService)

        // Post routes
        createPostRoute(postRepository)


    }



}
