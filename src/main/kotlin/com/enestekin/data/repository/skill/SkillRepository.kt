package com.enestekin.data.repository.skill

import com.enestekin.data.models.Skill

interface SkillRepository {

    suspend fun getSkills(): List<Skill>
}