package com.yewapp.data.network.api.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedLikeResponse(
    val list: List<FeedLike>,
    val countSummary: LikeCount
)

data class FeedLike(
    val createdAt: Int,
    val createdBy: CreatedByFeed,
    val feedId: String,
    val id: Int,
    val likeUnicode: String,
)

data class CreatedByFeed(
    val city: String,
    val country: String,
    val id: Int,
    val name: String,
    val profileImage: String,
)

data class LikeCount(
    @SerializedName("U+1245")
    @Expose
    val thumbsUp: String?,

    @SerializedName("U+1F62E")
    @Expose
    val surprised: String?,

    @SerializedName("U+1F642")
    @Expose
    val smile: String?,

    @SerializedName("U+1F603")
    @Expose
    val happy: String?,

    @SerializedName("U+1F622")
    @Expose
    val sad: String?,

    @SerializedName("U+2764")
    @Expose
    val heart: String?,

    )

