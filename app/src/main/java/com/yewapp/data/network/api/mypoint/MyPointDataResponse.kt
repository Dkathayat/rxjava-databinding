package com.yewapp.data.network.api.mypoint


data class UserPointSummaryResponse(
    val pageData: PageData,
    val list: List<UserPointSummaryData>
)

data class UserPointSummaryData(
    val category_id: String,
    val city_ids: String,
    val commentCount: String,
    val country_id: String,
    val created_at: String,
    val created_date: String,
    val description: String,
    val end_date: String,
    val icon: String,
    val likeCount: String,
    val location: String,
    val name: String,
    val sport_grade_level: String,
    val sport_name: String,
    val start_date: String,
    val state_id: String,
    val totalTime: String,
    val total_points: String,
    val type: String,
    val userCalories: String,
    val userDistance: String,
    val userElevation: String,
    val userLikeUnicode: String,
    val userTotalTime: String,
    val user_id: String
)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)