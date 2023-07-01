package com.yewapp.data.network.api.comment

data class CommentRequest(
    val feedId: Int,
    val parentId: Int,
    val comment: String
)