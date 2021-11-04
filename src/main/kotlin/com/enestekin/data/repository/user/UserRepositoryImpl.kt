package com.enestekin.data.repository.user

import com.enestekin.data.models.User
import com.enestekin.data.requests.UpdateProfileRequest
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or
import org.litote.kmongo.regex

class UserRepositoryImpl(
   db: CoroutineDatabase
) : UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun createUser(user: User) {
      users.insertOne(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.findOneById(id)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email) //eq equal
    }

    override suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    override suspend fun updateUser(
        userId: String,
        profileImageUrl: String,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {

        val user = getUserById(userId) ?: return false
        return users.updateOneById(
            id = userId,
            update = User(
            email = user.email,
            username = updateProfileRequest.username,
            password = user.password,
            profileImageUrl = profileImageUrl,
                bio = updateProfileRequest.bio,
                gitHubUrl = updateProfileRequest.gitHubUrl,
                instagramUrl =  updateProfileRequest.instagramUrl,
                linkedInUrl = updateProfileRequest.linkedInUrl,
                skills = updateProfileRequest.skills,
                followingCount = user.followerCount,
                postCount = user.postCount,
                id = user.id
            )
        ).wasAcknowledged()
    }

    override suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
        return  users.findOneById(userId)?.email == email
    }

    override suspend fun searchForUsers(query: String): List<User> {
        // (?i) makes it match case-insensitive
        return users.find(
            or(
                User::username regex Regex("(?i).*$query.*"),
                User::email eq query
            )
        )
            .descendingSort(User::followerCount)
            .toList()
    }

}