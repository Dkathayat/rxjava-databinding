package com.yewapp.data.network.api.mypoint

data class MyPointsHistoryResponse(
    val list: List<MyPointsHistoryDetails>,
    val pageData: PageData
)


data class MyPointsHistoryDetails (
    val _id: String,
    val created_at: String,
    val description: String,
    val distance: String,
    val location: String,
    val points: String,
    val sport_id: String,
    val sub_sport_id: String,
    val title: String,
    val user_id: String,
    @Transient
    var isSelected:Boolean
)

