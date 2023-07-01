package com.yewapp.data.network.api.associate

data class MigrateAssociateAccountRequest(
    val associateId: String,
    val confirmPassword: String,
    val newPassword: String,
    val oldPassword: String
)