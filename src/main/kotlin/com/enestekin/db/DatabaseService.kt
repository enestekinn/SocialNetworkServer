package com.enestekin.db

import com.enestekin.data.models.User

interface DatabaseService  {

    suspend fun createUser(user: User)

    suspend fun  getUserById(id: String): User


}