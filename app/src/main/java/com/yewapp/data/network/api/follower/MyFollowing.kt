package com.yewapp.data.network.api.follower


data class FollowingListingResponse(val pageData: PageData?, val list: List<MyFollowing>?)

data class MyFollowing(
    val userId: String?,
    val name: String?,
    val first_name: String?,
    val last_name: String?,
    val image: String?,
    val city: String?,
    val state: String?,
    val country: String?
)

