package com.enestekin.data.repository.post

import com.enestekin.data.models.Post
import com.enestekin.util.Constants

interface PostRepository {

    suspend fun createPostIfUserExists(post: Post): Boolean

    suspend fun  deletePost(postId: String)

    suspend fun getPostsByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int  = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post>
}