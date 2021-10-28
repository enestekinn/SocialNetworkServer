package com.enestekin.service

import com.enestekin.data.models.User
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.data.requests.CreateAccountRequest



// this class is basically like viewmodel
class UserService(
    private val repository: UserRepository
) {

suspend fun doesUserWithEmailExist(email: String): Boolean {
        return repository.getUserByEmail(email) != null
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
