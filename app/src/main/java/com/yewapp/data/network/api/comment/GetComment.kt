package com.yewapp.data.network.api.comment


data class CommentResponse(
    val list: List<Comment>
)

data class Comment(
    val comment: String,
    val createdAt: String,
    val createdBy: CreatedBy,
    val id: Int
)

data class CreatedBy(
    val id: Int,
    val name: String,
    val profileImage: String
)











