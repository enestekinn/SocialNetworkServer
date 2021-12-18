package com.enestekin.data.models

import com.enestekin.data.responses.SkillDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


data class User(

    val email: String,
    val username: String,
    val password: String,
    val profileImageUrl: String,
    val bannerUrl: String?,
    val bio: String,
    val gitHubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val postCount: Int = 0,
    val skills: List<SkillDto> = listOf(), // empty list by default
    @BsonId
    val id: String = ObjectId().toString(),


    )
