package com.yewapp.data.network.api.feed

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.yewapp.data.network.api.follower.PageData
import kotlinx.parcelize.Parcelize

data class FeedResponse(
    val pageData: PageData,
    val list: List<Feed>
)
@Parcelize
data class Feed(
    val id: Int,
    val isShortVideo:Boolean,
    val activityId:String,
    val title: String,
    val description: String,
    val latitude: String?,
    val longitude: String?,
    val location: String?,
    val createdAt: Long,
    val files: List<Files>?,
    val tags: List<Tags>?,
    val likeCount: Int,
    var likeUnicodes: List<String>?,
    val userLikeUnicode: String?,
    var commentCount: Int,
    val createdBy: CreatedBy?,
    @Transient
    var isWaiting: Boolean = false
) : Parcelable
@Parcelize
data class CreatedBy(
    val id: Int,
    val name: String,
    val profileImage: String?,
    var following: Boolean,
    var blocked: Boolean,
    var muted: Boolean,
    var favourite: Boolean,
) : Parcelable

@Parcelize
data class Files(
    val id: Int,
    val feed_id: String,
    val url: String,
    val type: String,
    val updated_at: String?
) : Parcelable
@Parcelize
data class Tags(
    val id: Int,
    @SerializedName("feed_id")
    @Expose
    val feedId: String,
    val tag: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable

data class FavoritesUserResponse(
    val pageData: PageData,
    val list: List<FavoriteList>
)

data class FavoriteList(
    val userId: Int?,
    val fullName: String?,
    val roleId: String?,
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val profileImage: String?
)

data class SuggestedUserResponse(
    val pageData: PageData,
    val list: List<SuggestedUserList>
)

data class SuggestedUserList(
    val userId: Int?,
    val fullName: String?,
    val roleId: String?,
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val profileImage: String?,
    val following: Boolean
)
























