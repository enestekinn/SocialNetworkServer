package com.enestekin.data.repository.post

import com.enestekin.data.models.Following
import com.enestekin.data.models.Post
import com.enestekin.data.models.User
import com.enestekin.util.Constants
import org.litote.kmongo.`in`
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.eq

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val posts = db.getCollection<Post>()
    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()

    override suspend fun createPostIfUserExists(post: Post): Boolean {
        val doesUserExist = users.findOneById(post.userId) != null

        if (!doesUserExist){
            return false
        }
        posts.insertOne(post)
        return true
    }

    override suspend fun deletePost(postId: String) {
        posts.deleteOneById(postId)
    }

    override suspend fun getPostsByFollows(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Post> {

        val userIdsFromFollows = following.find(Following::followedUserId eq userId)
            .toList()
            .map {
                it.followedUserId
        }

        return posts.find(
            Post::userId `in` userIdsFromFollows)
            .skip(page * pageSize) // skip first  15  elements
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()


    }
}