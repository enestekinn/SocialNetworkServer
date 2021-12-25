package com.enestekin.plugins

import com.enestekin.routes.*
import com.enestekin.service.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.content.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()
    val activityService: ActivityService by inject()
    val skillService: SkillService by inject()



    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()


    routing {

        // User routes
        authenticate()
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )
        searchUser(userService)
        getUserProfile(userService)
        getPostForProfile(postService)
        updateUserProfile(userService)

        // Following routes
        followUser(followService,activityService)
        unfollowUser(followService)

        // Post routes
        createPost(postService)
        getPostsForFollows(postService)
        deletePost(postService,likeService,commentService)
        getPostDetails(postService)

        // Like routes
        likeParent(likeService,activityService)
        unlikeParent(likeService, userService)
        getLikesForParent(likeService)

        //Comment routes
        createComment(commentService,activityService)
        deleteComment(commentService, likeService)
        getCommentsForPost(commentService)

        //Activity routes
        getActivities(activityService)

        // Skill routes
        getSkills(skillService)


        // provide everything in static folder
        // we created directory in static folder, so we implemented  static fun here
        static {
            resources("static")
        }
    }



}
