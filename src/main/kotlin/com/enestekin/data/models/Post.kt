package com.enestekin.data.models

import org.bson.codecs.pojo.annotations.BsonId

data class Post(
    @BsonId
    val id: String = Object().toString(),
    val imageUrl: String,
    val userId: String,
    val timestamp: Long,
    val description: String
)
