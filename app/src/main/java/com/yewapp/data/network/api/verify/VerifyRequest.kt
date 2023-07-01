package com.yewapp.data.network.api.verify

data class VerifyRequest(
    val email: String,
    val type: Int,
    val otp: String
)