package com.yewapp.data.network.api.spectator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class YewMemberResponse(
    val list: List<YewMember>,
    val pageData: PageData
)

data class YewMember(
    val blocked: Boolean?,
    val city: String?,
    val country: String?,
    val email: String?,
    val favourite: Boolean?,
    @SerializedName("first_name")
    @Expose
    val firstName: String?,
    val following: Boolean?,
    val fullName: String?,
    val last_name: String?,
    val muted: Boolean?,
    val profileImage: String?,
    val roleId: String?,
    val spectator: Boolean?,
    val state: String?,
    val userId: Int?,
    @Transient
    var isSelected: Boolean= false
)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)