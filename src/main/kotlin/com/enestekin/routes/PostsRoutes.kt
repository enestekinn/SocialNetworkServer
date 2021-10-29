package com.enestekin.routes


import com.enestekin.data.requests.CreatePostRequest
import com.enestekin.data.requests.DeletePostRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.plugins.email
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
    userService: UserService
){

    authenticate {
        post("/api/post/create"){
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post

            }
/*
when we attached a claim  which is email.user logs in . email saves in token.
 which user can't modifier.here we get that email from token
 *
 **/

                // same *
//            val email = call.principal<JWTPrincipal>()?.getClaim("email",String::class)
//            val isEmailByUser =userService.doesEmailBelongToUserId(
//                email =  email ?: "",
//                userId = request.userId
//            )
//
//            if (!isEmailByUser){
//                call.respond(HttpStatusCode.Unauthorized,"You are not who you say you are.")
//                return@post
//            }

                // same *
            println("${request.userId} %%%%% ${request.description}")
            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ){
                val didUserExist = postService.createPostIfUserExists(request)
                println("$didUserExist %%%%% .........")

                if (!didUserExist){
                    println("$didUserExist false")
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = false,
                            message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                }else {
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

}
fun Route.getPostsForFollows(
    postService: PostService,
    userService: UserService
){
   authenticate {
       get ("/api/post/get"){
           val userId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
               call.respond(HttpStatusCode.BadRequest)
               return@get
           }
           val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
           val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?:
           Constants.DEFAULT_POST_PAGE_SIZE



//ifEmailBelongsToUser  is basically validating  email shorter.
           ifEmailBelongsToUser(
               userId = userId,
               validateEmail = userService::doesEmailBelongToUserId
           ){
               println("Calisti")
               val posts = postService.getPostsForFollows(userId,page,pageSize)
               call.respond(
                   HttpStatusCode.OK,
                   posts
               )
           }

       }
   }
}

fun Route.deletePost(
    postService: PostService,
    userService: UserService,
    likeService: LikeService
){

    delete ("/api/post/delete") {
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
        ifEmailBelongsToUser(
            userId = post.userId,
            validateEmail = userService::doesEmailBelongToUserId
        ) {
            postService.deletePost(request.postId)
            likeService.deleteLikesForParent(request.postId)
            // TODO: 28.10.2021 Delete comments from post
            call.respond(HttpStatusCode.OK)
        }
    }
}