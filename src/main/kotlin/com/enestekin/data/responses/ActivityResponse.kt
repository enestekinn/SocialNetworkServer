package com.enestekin.data.responses

data class ActivityResponse(
    val timeStamp: Long,
    val userId: String,
    val parentId: String,
    val type: Int,
    val username: String,
    val id: String
)
