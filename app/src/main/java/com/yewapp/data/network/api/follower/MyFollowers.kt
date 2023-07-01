package com.yewapp.data.network.api.follower


data class FollowerListingResponse(val pageData: PageData?, val list: List<MyFollowers>)


data class PageData(
    val total: Int,
    val per_page: Int,
    val current_page: Int,
    val last_page: Int
)

data class MyFollowers(
    val userId: Int,
    val name: String,
    val first_name: String,
    val last_name: String,
    val image: String?,
    val city: String,
    val state: String,
    val country: String,
    var following: Boolean,
    var blocked: Boolean,
    var muted: Boolean,
    var favourite: Boolean
)

