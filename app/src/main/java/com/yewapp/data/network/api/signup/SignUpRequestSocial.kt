package com.yewapp.data.network.api.signup

data class SignUpRequestSocial(
    val deviceToken: String,
    val deviceType: String,
    val email: String,
    val provider: String,
    val provider_user_id: String,
    val dob:String?
)