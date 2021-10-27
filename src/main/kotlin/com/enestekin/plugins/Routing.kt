package com.enestekin.plugins

import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.routes.createUserRoute
import com.enestekin.routes.followUser
import com.enestekin.routes.loginUser
import com.enestekin.routes.unfollowUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userRepository: UserRepository by inject()
val followRepository: FollowRepository by inject()

    routing {

        // User routes
        createUserRoute(userRepository)
        loginUser(userRepository)

        // Following routes
        followUser(followRepository)
        unfollowUser(followRepository)


    }



}
