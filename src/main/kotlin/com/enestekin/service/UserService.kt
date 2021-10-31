package com.enestekin.service

import com.enestekin.data.models.User
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.data.requests.CreateAccountRequest
import com.enestekin.data.requests.LoginRequest


// this class is basically like viewmodel
class UserService(
    private val repository: UserRepository
) {

suspend fun doesUserWithEmailExist(email: String): Boolean {
        return repository.getUserByEmail(email) != null
    }

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean{
        return repository.doesEmailBelongToUserId(email,userId)
    }

    suspend fun getUserByEmail(email: String): User? {
        return repository.getUserByEmail(email)
    }
    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean{
        return  enteredPassword == actualPassword
    }

    suspend fun doesPasswordMatchForUser(request: LoginRequest):Boolean{
        return repository.doesPasswordForUserMatch(
            email = request.email,
            enteredPassword =  request.password
        )
    }


    suspend fun createUser(request: CreateAccountRequest) {
        repository.createUser(
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
