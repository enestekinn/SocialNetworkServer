package com.enestekin.routes

import com.enestekin.data.models.Post
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.requests.CreatePostRequest
import com.enestekin.util.Constants

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

    suspend fun getPostsForFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return repository.getPostsByFollows(userId, page, pageSize)
    }
}