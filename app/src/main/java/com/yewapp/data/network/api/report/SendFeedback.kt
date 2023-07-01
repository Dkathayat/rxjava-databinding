package com.yewapp.data.network.api.report

data class SendFeedback(
    val bugReport: String?,
    val featureRequest: String?,
    val image: String?,
    val sportsId: Int
)