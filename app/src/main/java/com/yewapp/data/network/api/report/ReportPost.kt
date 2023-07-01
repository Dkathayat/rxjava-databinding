package com.yewapp.data.network.api.report


data class ReportPost(
    val userId: Int?,
    val reasonId: Int?,
    val checkFollowerFollowing: Int?,
    val comment: String?,
    val attachments: List<String>?
)

data class CommentReportPost(
    val commentId: Int,
    val reasonId: Int,
    val comment: String,
    val attachments: List<String>?
)

data class SendFeedReport(
    val feedId: Int?,
    val reasonId: Int?,
    val comment: String?,
    val images: List<String>?
)

