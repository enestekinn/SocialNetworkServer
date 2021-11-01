package com.enestekin.data.repository.likes

import com.enestekin.data.models.Like
import com.enestekin.data.models.User
import com.enestekin.data.util.ParentType
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class LikeRepositoryImpl(
    db: CoroutineDatabase
) : LikeRepository {

    val likes = db.getCollection<Like>()
     private val users = db.getCollection<User>()

    override suspend fun likeParent(userId: String, parentId: String,parentType: Int): Boolean {

                val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist){
            likes.insertOne(Like(userId,parentId,parentType))
            true
        }else {
            false
        }
    }

   override suspend fun unlikeParent(userId: String, parentId: String): Boolean {

        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist){
            likes.deleteOne(
                and(
Like::userId eq userId,
                    Like::parentId eq parentId
                )
            )
            true
        }else {
            false
        }
    }

    override suspend fun deleteLikesForParent(parentId: String) {
        likes.deleteMany(Like::parentId eq parentId)
    }
}