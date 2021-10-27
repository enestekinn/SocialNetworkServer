package com.enestekin.plugins

import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.routes.*
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userRepository: UserRepository by inject()
val followRepository: FollowRepository by inject()
val postRepository: PostRepository by inject()

    routing {

        // User routes
        createUserRoute(userRepository)
        loginUser(userRepository)

        // Following routes
        followUser(followRepository)
        unfollowUser(followRepository)

        // Post routes
        createPostRoute(postRepository)


    }



}
