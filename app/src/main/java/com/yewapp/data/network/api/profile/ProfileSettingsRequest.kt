package com.yewapp.data.network.api.profile

data class ProfileSettingsRequest(
    val allowUserToSeeYourPoint: Boolean,
    val compareStatsPrivacy: Boolean,
    val hideActivityStartingLocation: Boolean,
    val hideEmailId: Boolean,
    val hidePhoneNumber: Boolean,
    val whoCanSeeYourStats: String
)