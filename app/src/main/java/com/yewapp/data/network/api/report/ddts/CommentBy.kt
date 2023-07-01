package com.yewapp.data.network.api.report.ddts

data class CommentBy(
    val city: String,
    val comment: String,
    val commentBy: CommentByX,
    val country: String,
    val createdByName: String,
    val feedDescription: String,
    val feedId: String,
    val feedTitle: String,
    val feedUrl: String,
    val id: Int,
    val profileImage: String,
    val state: String,
    val updateDate: Any
)