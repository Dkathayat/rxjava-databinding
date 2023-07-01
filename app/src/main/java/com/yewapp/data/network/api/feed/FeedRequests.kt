package com.yewapp.data.network.api.feed

data class CreateFeedRequest(
    val title: String,
    val description: String?,
    val isDraft: Boolean,
    val location: String,
    val latitude: String,
    val longitude: String,
    val activityId: String?,
    val hashtags: List<String>,
    val images: List<String>,
    val videos: List<String>
)

data class LikeFeedRequest(
    val feedId: Int,
    val likeUniCode: String
)
