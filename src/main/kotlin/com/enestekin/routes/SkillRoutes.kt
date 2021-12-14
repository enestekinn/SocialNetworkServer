package com.enestekin.routes

import com.enestekin.service.SkillService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getSkills(skillService: SkillService) {

    // user must be authenticated
    authenticate {
        get("/api/skills/get"){
            call.respond(
                HttpStatusCode.OK,
                skillService.getSkills().map { it.toSkillResponse() }
            )
        }
    }
}