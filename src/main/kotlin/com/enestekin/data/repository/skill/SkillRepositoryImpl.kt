package com.enestekin.data.repository.skill

import com.enestekin.data.models.Skill
import org.litote.kmongo.coroutine.CoroutineDatabase

class SkillRepositoryImpl(
    private val db: CoroutineDatabase
) : SkillRepository {

    private val skills = db.getCollection<Skill>()


    override suspend fun getSkills(): List<Skill> {

        return  skills.find().toList()

    }

    }
