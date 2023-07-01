package com.yewapp.data.network.api.comment

data class GetFeedReportedComments(
    val list: List<ReportedComments>,
    val code:String
)

data class Data(
    val list: List<ReportedComments>
)

data class ReportedComments (
    val comment: String,
    val commentBy: CommentBy,
    val createdAt: Long,
    val createdBy: FeedCreatedBy,
    val feedId: Int,
    val feeds: Feeds,
    val id: Int,
    val status: String
)

data class FeedCreatedBy(
    val address: String,
    val id: Int,
    val name: String,
    val profileImage: String
)

data class CommentBy(
    val address: String,
    val id: Int,
    val name: String,
    val profileImage: String
)

data class Feeds(
    val description: String,
    val feedType: String,
    val id: Int,
    val location: String,
    val sportActivityIcon: String,
    val timeAgo: String,
    val title: String,
    val url: String
)
