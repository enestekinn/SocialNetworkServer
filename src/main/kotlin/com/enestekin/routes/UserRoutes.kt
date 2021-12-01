package com.enestekin.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.enestekin.data.models.User
import com.enestekin.data.requests.CreateAccountRequest
import com.enestekin.data.requests.LoginRequest
import com.enestekin.data.requests.UpdateProfileRequest
import com.enestekin.data.responses.AuthResponse
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.service.PostService
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import com.enestekin.util.ApiResponseMessages.FIELDS_BLANK
import com.enestekin.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.enestekin.util.ApiResponseMessages.USER_ALREADY_EXISTS
import com.enestekin.util.Constants
import com.enestekin.util.Constants.BASE_URL
import com.enestekin.util.Constants.PROFILE_PICTURE_PATH
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
import java.util.*



fun Route.searchUser(userService: UserService){

    authenticate {
        get ("/api/user/search"){
            val query = call.parameters[QueryParams.PARAM_QUERY]
            if(query == null || query.isBlank()){
                call.respond(
                    HttpStatusCode.OK,
                    listOf<User>()
                )
                return@get

            }

            val searchResults = userService.searchForUsers(query,call.userId)
            call.respond(
                HttpStatusCode.OK,
                searchResults
            )


        }
    }
}

fun Route.getPostForProfile(
    postService: PostService,
) {
    authenticate {
        get("/api/user/posts") {


            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE


            val posts = postService.getPostsForProfile(
                userId = call.userId,
                page = page,
                pageSize = pageSize

            )
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }

    }
}

fun Route.getUserProfile(userService: UserService){

    authenticate {
        get ("/api/user/profile"){
            val userId = call.parameters[QueryParams.PARAM_USERID]
            if(userId == null || userId.isBlank()){
                call.respond(HttpStatusCode.BadRequest)
                return@get

            }

            val profileResponse = userService.getUserProfile(userId, call.userId)
            if (profileResponse == null){
                call.respond(HttpStatusCode.OK,BasicApiResponse<Unit>(
                    successful = false,
                    message = ApiResponseMessages.USER_NOT_FOUND
                )
                )
                return@get

            }
            call.respond(
                HttpStatusCode.OK,
                profileResponse
            )


        }
    }
}

fun Route.updateUserProfile(userService: UserService) {

    val gson: Gson by inject()

    authenticate {
        put("/api/user/update") {


            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var fileName: String? = null

            multipart.forEachPart { partData ->

                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                             updateProfileRequest = gson.fromJson<UpdateProfileRequest>(
                                partData.value,
                                UpdateProfileRequest::class.java
                            )
                        }

                    }
                    is PartData.FileItem -> {
                fileName =  partData.save(PROFILE_PICTURE_PATH)
                    }
                    is PartData.BinaryItem -> Unit
                }
            }


            val profilePictureUrl = "${BASE_URL}profile_pictures/$fileName"

            updateProfileRequest?.let { request ->

                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl =  profilePictureUrl,
                    updateProfileRequest = request
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${PROFILE_PICTURE_PATH}/$fileName").delete()
                    call.respond(
                        HttpStatusCode.InternalServerError
                    )
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }


        }
    }
}

