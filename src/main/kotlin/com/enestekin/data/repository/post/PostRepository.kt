package com.enestekin.data.repository.post

import com.enestekin.data.models.Post
import com.enestekin.data.responses.PostResponse
import com.enestekin.util.Constants

interface PostRepository {

    suspend fun createPost(post: Post): Boolean

    suspend fun  deletePost(postId: String)

    suspend fun getPostsByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int  = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post>

    suspend fun getPostsForProfile(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int  = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse>


    suspend fun getPost(postId: String): Post?

    suspend fun getPostDetails(userId: String,postId: String): PostResponse?

}