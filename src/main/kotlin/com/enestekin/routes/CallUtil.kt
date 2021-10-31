package com.enestekin.routes

import com.enestekin.plugins.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*

val ApplicationCall.userId: String
get() = principal<JWTPrincipal>()?.userId.toString()