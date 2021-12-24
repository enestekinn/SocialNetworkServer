package com.enestekin.routes


import com.enestekin.data.requests.CreatePostRequest
import com.enestekin.data.requests.DeletePostRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.service.CommentService
import com.enestekin.service.LikeService
import com.enestekin.service.PostService
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import com.enestekin.util.Constants
import com.enestekin.util.Constants.POST_PICTURE_PATH
import com.enestekin.util.QueryParams
import com.enestekin.util.save
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.createPost(
    postService: PostService
) {

    val gson by inject<Gson>()

    authenticate {

        post("/api/post/create") {


            val multipart = call.receiveMultipart()
            var createPostRequest: CreatePostRequest? = null
            var fileName: String? = null
                multipart.forEachPart { partData ->

                    when (partData) {
                        is PartData.FormItem -> {

                            println(partData.name)
                            //  using post_data  in postman
                            if (partData.name == "post_data") {

                                createPostRequest = gson.fromJson(
                                    partData.value,
                                    CreatePostRequest::class.java
                                )

                            }

                        }
                        is PartData.FileItem -> {
                          fileName =  partData.save(POST_PICTURE_PATH)
                        }
                        is PartData.BinaryItem -> Unit
                    }
                }


            val postPictureUrl = "${Constants.BASE_URL}post_pictures/$fileName"


            createPostRequest?.let { request ->

                val createPostAcknowledged = postService.createPost(
                    request = request,
                    userId = call.userId,
                    imageUrl = postPictureUrl

                )
                println(createPostAcknowledged)
                if (createPostAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${POST_PICTURE_PATH}/$fileName").delete()
                    call.respond(
                        HttpStatusCode.InternalServerError
                    )
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }

    }
}
fun Route.getUserProfile(userService: UserService) {

    authenticate {
        get("/api/user/profile") {

            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            if (userId == null || userId.isBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get

            }
            println("profileResponse")
            val profileResponse = userService.getUserProfile(userId, call.userId)
            println("profileResponse: $profileResponse UserRoutes")

            if (profileResponse == null) {
                call.respond(
                    HttpStatusCode.OK, BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get

            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = profileResponse
                )
            )


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
            if (post.userId == call.userId) {

                postService.deletePost(request.postId)
                likeService.deleteLikesForParent(request.postId)
                commentService.deleteCommentsForPost(request.postId)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }

        }
    }
}
    fun Route.getPostDetails(postService: PostService){
        authenticate {
            get("/api/post/details"){
                val postId = call.parameters["postId"] ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val post = postService.getPostDetails(call.userId,postId) ?: kotlin.run {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = post
                    )
                )
            }
        }
    }
