package com.yewapp.data.network.api.forgotpassword

data class ForgotPasswordRequest(
    val email: String
)

data class ChangePasswordRequest(
    val email: String,
    val token: String,
    val password: String
)