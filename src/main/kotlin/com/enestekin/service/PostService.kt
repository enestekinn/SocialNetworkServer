package com.enestekin.service

import com.enestekin.data.models.Post
import com.enestekin.data.repository.post.PostRepository
import com.enestekin.data.requests.CreatePostRequest
import com.enestekin.util.Constants

class PostService (
    private val repository: PostRepository
        ) {

    suspend fun  createPost(
        request: CreatePostRequest,
        userId: String,
        imageUrl: String
    ): Boolean{
      return   repository.createPost(
            Post(
                imageUrl = imageUrl,
                userId = userId,
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
        return repository.getPostsForProfile(userId, page, pageSize)
    }

    suspend fun getPostsForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return repository.getPostsByFollows(userId, page, pageSize)
    }
    suspend fun getPost(postId: String): Post? = repository.getPost(postId)

    suspend fun deletePost(postId: String){
        repository.deletePost(postId)
    }
}