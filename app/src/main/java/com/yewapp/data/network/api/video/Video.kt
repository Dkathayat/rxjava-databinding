package com.yewapp.data.network.api.video

data class SaveVideoRequest(
    val description: String,
    val hashtags: List<String>,
    val isDraft: Boolean,
    val title: String,
    val videoUrl: String,
    val visibility: String
)

//-----------------------------------Video list request
data class GetVideoListRequest(
    val description: String,
    val hashtags: List<String>,
    val isDraft: Boolean,
    val title: String,
    val videoUrl: String,
    val visibility: String
)
//-----------------------------------Video list response


data class VideoListResponse(
    val list: List<VideoData>,
    val pageData: PageData
)

data class VideoData(
    var commentCount: Int,
    val createdAt: Int,
    val createdBy: CreatedBy,
    val description: String,
    val files: List<File>,
    val id: Int,
    val latitude: String,
    val likeCount: Int,
    var shareCount: Int,
    val likeUnicodes: List<Any>,
    val location: String,
    val longitude: String,
    val tags: List<Tag>,
    val title: String,
    val userLikeUnicode: Any
)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)

data class CreatedBy(
    val blocked: Boolean,
    val favourite: Boolean,
    var following: Boolean,
    val id: Int,
    val muted: Boolean,
    val name: String,
    val profileImage: String,
    val country: String,
    val state: String,
    val city: String
)

data class File(
    val feed_id: String,
    val id: Int,
    val type: String,
    val url: String
)

data class Tag(
    val feed_id: String,
    val id: Int,
    val tag: String,
    val updated_at: String
)
//---------------------------------video comment

data class CommentListResponse(
    val list: List<Comment>
)

//data class Comment(
//    val comment: String,
//    val createdAt: String,
//    val createdBy: CreatedBy,
//    val feed_id: Int,
//    val id: Int
//)

data class Comment(
    val id: Int,
    val feedId: Int?,
    val comment: String?,
    val createdAt: String,
    val createdBy: CreatedBy,
    val replyCount: Int?,
    val likeCount: Int?,
    val likeStatus: Boolean,
    val reply: List<Reply>?
)

data class Reply(
    val id: Int,
    val comment: String?,
    val createdAt: String,
    val createdBy: CreatedBy,
    val likeCount: Int?,
    val likeStatus: Boolean
)


data class LikeCommentRequest(
    val commentId: Int
)
