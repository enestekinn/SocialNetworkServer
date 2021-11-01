package com.enestekin.routes


import com.enestekin.data.requests.CreatePostRequest
import com.enestekin.data.requests.DeletePostRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.service.CommentService
import com.enestekin.service.LikeService
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import com.enestekin.util.Constants
import com.enestekin.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPost(
    postService: PostService,
) {

    authenticate {
        post("/api/post/create") {
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post

            }
/*
when we attached a claim  which is email.user logs in . email saves in token.
 which user can't modifier.here we get that email from token
 *
 **/


            val userId = call.userId

            val didUserExist = postService.createPostIfUserExists(request,userId)

            if (!didUserExist) {
                println("$didUserExist false")
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                    )
                )
            }
        }

    }
}


fun Route.getPostsForFollows(
    postService: PostService,
) {
    authenticate {
        get("/api/post/get") {


            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE


            val posts = postService.getPostsForFollows(call.userId, page, pageSize)
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }

    }
}


fun Route.deletePost(
    postService: PostService,
    likeService: LikeService,
    commentService: CommentService
) {
    authenticate {
    delete("/api/post/delete") {
        val request = call.receiveOrNull<DeletePostRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete

        }

        val post = postService.getPost(request.postId)


        if (post == null) {
            call.respond(
                HttpStatusCode.NotFound
            )
            return@delete
        }
        if (post.userId == call.userId){

            postService.deletePost(request.postId)
            likeService.deleteLikesForParent(request.postId)
            commentService.deleteCommentsForPost(request.postId)
            call.respond(HttpStatusCode.OK)
        }else {
            call.respond(HttpStatusCode.Unauthorized)
        }

    }
    }


}