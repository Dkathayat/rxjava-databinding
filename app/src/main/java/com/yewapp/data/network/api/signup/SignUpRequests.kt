package com.yewapp.data.network.api.signup

data class SignUpRequest(
    val email: String,
    val password: String,
    val referralCode: String,
    val dob: String,
    val deviceType: Int = 1, //Device Type: 1 = Android, 2 = IOS
    val deviceToken: String
)
