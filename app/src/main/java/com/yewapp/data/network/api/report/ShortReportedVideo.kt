package com.yewapp.data.network.api.report


data class GetShortReportedVideo(
    val list: List<ShortReportedVideo>,
    val commentBy: List<ReportedComment>,
    val pageData: PageDataShort
)

data class ShortReportedVideo(
    val createdAt: Long,
    val createdBy: CreatedByShort,
    val description: String,
    val files: List<ShortFile>,
    val id: Int,
    val sportActivityIcon: String,
    val status: String,
    val title: String
)

data class CreatedByShort(
    val blocked: Boolean,
    val city: String,
    val country: String,
    val favourite: Boolean,
    val following: Boolean,
    val gender: String,
    val id: Int,
    val muted: Boolean,
    val name: String,
    val profileImage: String,
    val state: String
)

data class ShortFile(
    val feed_id: String,
    val id: Int,
    val type: String,
    val url: String
)

data class ReportedComment(
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
    val updateDate: Long
)

data class CommentByX(
    val CityName: String,
    val country: String,
    val fullName: String,
    val profileImage: String,
    val state: String
)
data class PageDataShort(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)