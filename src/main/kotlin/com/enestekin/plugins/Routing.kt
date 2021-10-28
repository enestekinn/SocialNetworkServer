package com.enestekin.plugins

import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.routes.*
import com.enestekin.service.FollowService
import com.enestekin.service.UserService
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.auth.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userService: UserService by inject()

    val followService: FollowService by inject()

    val postService: PostService by inject()



    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {

        // User routes
        createUserRoute(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following routes
        followUser(followService)
        unfollowUser(followService)

        // Post routes
        createPostRoute(postService,userService)
        getPostsForFollows(postService, userService)


    }



}
