package com.yewapp.data.network.api.report

import com.yewapp.data.network.api.feed.Files


data class GetReportedPost(
    val list: List<ReportedPosts>,
    val pageData: PageData
)

data class ReportedPosts(
    val commentCount: Int,
    val createdAt: Long,
    val createdBy: ReportedFiles,
    val description: String,
    val feedType: String,
    val files: List<ReportedFeedFile>,
    val id: Int,
    val isShortVideo:Boolean,
    val latitude: String,
    val likeCount: Int,
    val likeUnicodes: List<String>,
    val location: String?,
    val longitude: String,
    val sportActivityIcon: String?,
    val status: String,
    val timeAgo: String,
    val title: String,
    val userLikeUnicode: String
)

data class ReportedFiles(
    val blocked: Boolean,
    val city: String,
    val country: String,
    val favourite: Boolean,
    val following: Boolean,
    val gender: Any,
    val id: Int,
    val muted: Boolean,
    val name: String,
    val profileImage: String?,
    val state: String
)

data class ReportedFeedFile(
    val feed_id: String,
    val id: Int,
    val type: String,
    val url: String

)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)

data class DeleteComment(
    val commentId:Int
)
