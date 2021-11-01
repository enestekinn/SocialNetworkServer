package com.enestekin.plugins

import com.enestekin.routes.*
import com.enestekin.service.*
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()
    val activityService: ActivityService by inject()



    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {

        // User routes
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )
        searchUser(userService)

        // Following routes
        followUser(followService,activityService)
        unfollowUser(followService)

        // Post routes
        createPost(postService)
        getPostsForFollows(postService)
        deletePost(postService,likeService,commentService)

        // Like routes
        likeParent(likeService,activityService)
        unlikeParent(likeService, userService)

        //Comment routes
        createComment(commentService,activityService)
        deleteComment(commentService, likeService)
        getCommentsForPost(commentService)

        //Activity routes
        getActivities(activityService)

    }



}
