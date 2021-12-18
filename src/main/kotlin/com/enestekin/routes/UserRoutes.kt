package com.enestekin.routes

import com.enestekin.data.requests.UpdateProfileRequest
import com.enestekin.data.responses.BasicApiResponse
import com.enestekin.data.responses.UserResponseItem
import com.enestekin.service.PostService
import com.enestekin.service.UserService
import com.enestekin.util.ApiResponseMessages
import com.enestekin.util.Constants
import com.enestekin.util.Constants.BANNER_IMAGE_PATH
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


fun Route.searchUser(userService: UserService) {

    authenticate {
        get("/api/user/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY]
            if (query == null || query.isBlank()) {
                call.respond(
                    HttpStatusCode.OK,
                    listOf<UserResponseItem>()
                )
                return@get

            }

            val searchResults = userService.searchForUsers(query, call.userId)
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

            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            println("/api/user/posts : userId $userId")
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE


            val posts = postService.getPostsForProfile(
                userId = userId ?: call.userId,
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



fun Route.updateUserProfile(userService: UserService) {

    val gson: Gson by inject()

    authenticate {
        put("/api/user/update") {


            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var profilePictureFileName: String? = null
            var bannerImageFileName: String? = null

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
                        if (partData.name == "profile_picture") {

                            profilePictureFileName = partData.save(PROFILE_PICTURE_PATH)

                        } else if (partData.name == "banner_image") {

                            bannerImageFileName = partData.save(BANNER_IMAGE_PATH)

                        }
                    }
                    is PartData.BinaryItem -> Unit
                }
            }


            val profilePictureUrl = "${BASE_URL}profile_pictures/$profilePictureFileName"
            val bannerImageUrl = "${BASE_URL}banner_images/$bannerImageFileName"

            updateProfileRequest?.let { request ->

                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl = if (profilePictureFileName == null) {
                        null
                    } else {
                        profilePictureUrl
                    },
                    bannerUrl = if (bannerImageFileName == null) {
                        null
                    } else {
                        bannerImageUrl
                    },
                    updateProfileRequest = request,
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${PROFILE_PICTURE_PATH}/$profilePictureFileName").delete()
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

