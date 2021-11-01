package com.enestekin.service

import com.enestekin.data.models.User
import com.enestekin.data.repository.follow.FollowRepository
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.data.requests.CreateAccountRequest
import com.enestekin.data.responses.UserResponseItem


// this class is basically like viewmodel
class UserService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

suspend fun doesUserWithEmailExist(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean{
        return userRepository.doesEmailBelongToUserId(email,userId)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }
    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean{
        return  enteredPassword == actualPassword
    }

suspend fun  searchForUsers(query: String,userId: String): List<UserResponseItem>{
    val users = userRepository.searchForUsers(query)
    val followsByUser = followRepository.getFollowsByUser(userId)
    return users.map { user->
        val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
    UserResponseItem(
        username = user.username,
        profilePictureUrl = user.profileImageUrl,
        bio =  user.bio,
        isFollowing = isFollowing
    )

    }

}


    suspend fun createUser(request: CreateAccountRequest) {
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                gitHubUrl = null,
                instagramUrl = null,
                linkedInUrl = null
            )
        )
    }
    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvent{
        // we have two validates success , field empty
        if(request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return     ValidationEvent.Success

    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object Success : ValidationEvent()
    }
}
