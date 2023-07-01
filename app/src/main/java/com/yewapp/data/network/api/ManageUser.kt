package com.yewapp.data.network.api

import com.yewapp.data.network.api.follower.MyFollowers


data class ManageUserListResponse(val pageData: PageData?, val list: List<UserList>?)

data class UserList(
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

data class PageData(
    val total: Int?,
    val per_page: Int?,
    val current_page: Int?,
    val last_page: Int?
)


data class FollowUpdateResponse(val details: List<UserList>?)
data class FollowersUpdateResponse(val details: List<MyFollowers>?)

//data class Details(val userId: Int?, val fullName: String?, val roleId: String?, val first_name: String?, val last_name: String?, val email: String?, val city: String?, val state: String?, val country: String?, val profileImage: String?, val following: Boolean)