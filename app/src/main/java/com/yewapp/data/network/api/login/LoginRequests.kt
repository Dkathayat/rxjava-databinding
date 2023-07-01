package com.yewapp.data.network.api.login

data class LoginRequest(
    val email: String,
    val password: String,
    val deviceType: Int = 1, //Device Type: 1 = Android, 2 = IOS
    val deviceToken: String
)