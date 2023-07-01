package com.yewapp.data.network.api.emailchange

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ChangeEmailRequest(
    val email: String
)


data class ChangePhoneRequest(
    @SerializedName("mobile")
    @Expose
    val mobileNumber: String,
    val countryId: String
)

data class UpdateEmailRequest(
    val email: String,
    val otp: String
)

data class UpdatePhoneRequest(
    @SerializedName("mobile")
    @Expose
    val mobileNumber: String,
    val countryId: String,
    val otp: String
)


data class ChallengeNameRequest(
    val challengeId: String,
    val challengeName: String
)

data class ChallengeNameResponse(
    val isChallengeNameAvailable: Boolean
)