package com.yewapp.data.network.api.signup

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class SignUpResponse(
    val Challenge: Challenge,
    val activities: Activities,
//    val aws: String,
    @SerializedName("aws_testing")
    @Expose
    val awsCredential: AwsCredential,
    val profile: Profile,
//    val privacySetting: PrivacySetting,
    val subscriptionDetail: SubscriptionDetail,
    val token: Token
)

data class Challenge(
    val totalChallengeCount: Int?
)

data class AwsCredential(
    val bucket_key: String,
    val bucket_name: String,
    val bucket_secret_key: String,
    val region: String
)

@Parcelize
data class Profile(
    val address: String?,
    val allowUserToSeeYourPoint: Boolean?,
    val bio: String?,
    val city: String?,
    val cityId: Int?,
    val compareStatsPrivacy: Boolean?,
    val country: String?,
    val countryId: Int?,
    val dob: String?,
    val email: String?,
    val emailVerified: Boolean?,
    val firstName: String?,
    val followers: Int?,
    val following: Int?,
    val gender: String?,
    val heartRate: String?,
    val hideActivityStartingLocation: Boolean?,
    val hideEmailId: Boolean?,
    val hidePhoneNumber: Boolean?,
    val isNotificationEnabled: Int?,
    val lastName: String?,
    val latitude: String?,
    val longitude: String?,
    val mobileVerified: Boolean?,
    val phone: String?,
    val pincode: String?,
    val profileCoverImage: String?,
    val profileImage: String?,
    val state: String?,
    val stateId: Int?,
    var userId: Int?,
    val weight: String?,
    val whoCanSeeYourStats: String?,
    val isFreeSubscription: Boolean?,
    val isAssociate: Boolean?
) : Parcelable

//data class PrivacySetting(
//    val allowCompareStats: Boolean,
//    val statusVisibility: Int,
//    val pointsVisibility: Boolean,
//    val hideNumber: Boolean,
//    val hideEmail: Boolean,
//    val hideActivityLocation: Boolean
//)

data class Activities(
    val biking: Int,
    val calories: Double,
    val distance: Int,
    val elevationGain: String,
    val hiking: Int,
    val running: Int,
    val time: String
)

data class SubscriptionDetail(
    val Id: Int,
    val IsReferFriend: Boolean,
    val description: String,
    val duration: String,
    val feedVideoSize: String,
    val isBeacon: Boolean,
    val isChat: Boolean,
    val isCompareStats: Boolean,
    val isFeed: Boolean,
    val isJoinChallenge: Boolean,
    val isLeaderboardView: Boolean,
    val isManageChallenge: Boolean,
    val isManageDeviceConnectivity: Boolean,
    val isManageFeedback: Boolean,
    val isManageFollowers: Boolean,
    val isManageFollowing: Boolean,
    val isManageFreeAds: Boolean,
    val isManageRoute: Boolean,
    val isRecordActivity: Boolean,
    val isShowParticipantsPicOnTrail: Boolean,
    val isViewHiddenEggs: Boolean,
    val isViewNearbyBikers: Boolean,
    val maxChallengeLimit: String,
    val maxSpectatorLimit: String,
    val maximumAssociateAccountLimit: String,
    val name: String,
    val recordingLimit: String,
    val status: Boolean
)


data class Token(
    @SerializedName("token_type") var tokenType: String,
    @SerializedName("expires_in") var expiresIn: Int,
    @SerializedName("access_token") var accessToken: String,
    @SerializedName("refresh_token") var refreshToken: String

)


//data class SignUpResponse(
//    val profile: UserProfile,
//    val activities: Activities,
//    val privacySetting: PrivacySetting,
//    var token: Token
//)
//
//
