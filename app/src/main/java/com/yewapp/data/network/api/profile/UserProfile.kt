package com.yewapp.data.network.api.profile

import com.yewapp.data.network.api.signup.*


data class UserProfileResponse(
    val Challenge: Challenge,
    val activities: Activities,
    val profile: Profile,
    val privacySetting: PrivacySetting,
    val subscriptionDetail: SubscriptionDetail
)


//
//data class UserProfileResponse(
//    val profile: UserProfile,
//    val activities: Activities,
//    val privacySetting: PrivacySetting
//)
//
//// result generated from /json
//
//data class Activities(
//    val biking: Int,
//    val hiking: Int,
//    val running: Int,
//    val calories: Double,
//    val distance: Int,
//    val time: String?,
//    val elevationGain: String?
//)
//
///*data class Data(
//    val profile: Profile?,
//    val activities: Activities?,
//    val privacySetting: PrivacySetting?
//)*/
//
//data class PrivacySetting(
//    val allowCompareStats: Boolean,
//    val statusVisibility: Int,
//    val pointsVisibility: Boolean,
//    val hideNumber: Boolean,
//    val hideEmail: Boolean,
//    val hideActivityLocation: Boolean
//)
//
//data class UserProfile(
//    val userId: Int,
//    val firstName: String?,
//    val lastName: String?,
//    val phone: String?,
//    val email: String?,
//    val dob: String?,
//    val gender: String?,
//    val profileImage: String?,
//    val profileCoverImage: String?,
//    val weight: String?,
//    val heartRate: String?,
//    val address: String?,
//    val pincode: String?,
//    val cityId: Int?,
//    val city: String,
//    val state: String,
//    val stateId: Int,
//    val country: String,
//    val countryId: Int,
//    val bio: String?,
//    val isNotificationEnabled: Int,
//    val mobileVerified: Boolean,
//    val emailVerified: Boolean,
//    val followers: Int,
//    val following: Int
//)
