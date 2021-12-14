package com.enestekin.service

import com.enestekin.data.models.Skill
import com.enestekin.data.repository.skill.SkillRepository

class SkillService (
    private val repository: SkillRepository
        ){

    suspend fun getSkills(): List<Skill>{
        return repository.getSkills()
    }
}