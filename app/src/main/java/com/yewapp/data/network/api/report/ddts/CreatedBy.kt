package com.yewapp.data.network.api.report.ddts

data class CreatedBy(
    val blocked: Boolean,
    val city: String,
    val country: String,
    val favourite: Boolean,
    val following: Boolean,
    val gender: String,
    val id: Int,
    val muted: Boolean,
    val name: String,
    val profileImage: Any,
    val state: String
)