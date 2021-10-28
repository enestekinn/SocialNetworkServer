package com.enestekin.routes

import com.enestekin.data.models.Post
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.requests.CreatePostRequest

class PostService (
    private val repository: PostRepository
        ) {

    suspend fun  createPostIfUserExists(request: CreatePostRequest): Boolean{
      return   repository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

}