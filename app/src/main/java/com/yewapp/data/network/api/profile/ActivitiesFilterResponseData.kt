package com.yewapp.data.network.api.profile

data class ActivitiesFilterResponseData(
    val list: List<ActivitiesData>?,
    val pageData: PageData,
    val summary: ActivitySummary
)

data class ActivitiesData(
    val activeDuration: String,
    val activityDescription: String?,
    val activityFeedId: Any,
    val activityId: String,
    val activityName: String,
    val activityRunning: Boolean,
    val activityType: String,
    val activity_image: String,
    val categoryId: String,
    val cityId: String,
    val commentCount: String,
    val countryId: String,
    val created_at: String,
    val end_date: Int,
    val icon: String,
    val likeCount: String,
    val likeUnicodes: List<String>,
    val location: String,
    val sharedAsFeed: Boolean,
    val sportGradeLevel: String,
    val sportName: String,
    val start_date: Int,
    val stateId: String,
    val type: String,
    val userCalorie: String,
    val userDistance: String,
    val userElevation: String,
    val userId: String,
    val userLikeUnicode: Any,
    @Transient
    var isWaiting: Boolean = false
)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)

data class ActivitiesFilterRequest(
    val user_id: String?,
    val start_date: String?,
    val end_date: String?,
    val sports: List<String>?
)



data class FilterData(
    val name: String,
    val filterDatatype:String,
    var isSelected: Boolean
)

data class ActivitySummary(
    val ather: Int,
    val ather222: Int,
    val biking: Int,
    val calories: String,
    val distance: String,
    val elevationGain: String,
    val hiking: Int,
    val test: Int,
    val time: String,
    val total_points: Int
)