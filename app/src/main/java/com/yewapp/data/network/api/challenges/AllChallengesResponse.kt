package com.yewapp.data.network.api.challenges

/**
 * @author Narbir singh
 * @param   We pass ChallengeEnum to get all type of challenges
 * @description  class is used to get response for all type of challenges like Created, Active, Favorites, Past,Upcoming etc.
 */


data class AllChallengesResponse(
    val list: List<ChallengesDetails>,
    val pageData: PageData
)

data class ChallengesDetails(
    val additionalInformation: String,
    val ageGroup: List<String>,
    val bannerImage: String,
    val challengeStatus: Boolean,
    val challengeType: String,
    val city: String,
    val cityIDS: String,
    val combinedRewardType: String,
    val combinedRewardValue: String,
    val country: String,
    val countryID: String,
    val createdAt: String,
    val description: String,
    val endDate: String,
    val femaleRewardType: String,
    val femaleRewardTypeValue: String,
    val icon: String,
    val id: Int,
    val importantNote: String,
    val inviteUserID: List<String>,
    val isDraft: Boolean,
    val isEarnPoint: Boolean,
    val location: String,
    val locationLatitude: String,
    val locationLongitude: String,
    val maleRewardType: String,
    val maleRewardTypeValue: String,
    val maxUserLimit: Int,
    val minimumAvgWatt: String,
    val minimumCalories: String,
    val minimumElevationGain: String,
    val minimumMiles: String,
    val minimumPointsToJoin: String,
    val minimumTime: String,
    val name: String,//challenge name
    val overview: String,
    val prize: String,
    val prizeType: String,
    val qualificationCriteria: String,
    val routeID: String,
    val rules: String,
    val sponsorName: String,
    val sportGradeLevel: List<String>,
    val sportType: String,
    val sportTypeID: Int,
    val sportTypeIcon: String,
    val startDate: String,
    val state: String,
    val stateID: String,
    val subSportTypeID: Int,
    val totalTime: String,
    val userCalories: String,
    val userDistance: String,
    val userElevation: String,
    val userRank: String,
    val userTotalTime: String,
    val visibility: String,
    val winnerPrizeAwarded: String,
    val winnerType: String,
    val winningCriteria: String,
    val zipcodes: List<String>
)

data class PageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)


//data class AllChallengesResponse(
//    val list: List<ChallengesDetails>,
//    val pageData: PageData
//)
//
//data class ChallengesDetails(
//    val ageGroup: List<AgeGroup>?,
//    @SerializedName("bannerImage")
//    val challengeProfileIcon: String?,
//    val challengeType: String?,
//    val city: String?,
//    val country: String?,
//    val createdAt: String?,
//    val description: String?,
//    val endDate: String?,
//    @SerializedName("icon")
//    val sportTypeIcon: String?,
//    val id: Int?,
//    val importantNote: String?,
//    val isEarnPoint: Boolean?,
//    val location: String?,
//    val maxUserLimit: Int?,
//    val minimumAvgWatt: String?,
//    val minimumCalories: String?,
//    val minimumElevationGain: String?,
//    val minimumMiles: String?,
//    @SerializedName("name")
//    val challengeName: String?,
//    val sportGradeLevel: String?,
//    val sportType: String?,
//    val startDate: String?,
//    val state: String?,
//    val totalTime: Int?,
//    val winningCriteria: String?,
//
//    val isDraft: Boolean?,
//    val minimumPointsToJoin: String?,
//    val sponsorName: String?,
//    val userCalories: String?,
//    val userDistance: String?,
//    val userTotalTime: String?,
//    val rewardType: String?,
//    val rewardTypeValue: String?,
//    val userElevation: String?,
//    val userRank: String?,
//    val challengeStatus: Boolean?
//)
//
//data class PageData(
//    val current_page: Int,
//    val last_page: Int,
//    val per_page: Int,
//    val total: Int
//)
//
//data class AgeGroup(
//    val age: String
//)