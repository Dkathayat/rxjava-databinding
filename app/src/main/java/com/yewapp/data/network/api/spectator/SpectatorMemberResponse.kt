package com.yewapp.data.network.api.spectator


data class SpectatorMemberResponse(
    val list: List<SpectatorMember>,
)

data class SpectatorMember(
    val city: String,
    val country: String,
    val email: String,
    val firstName: String,
    val fullName: String,
    val profileImage: String,
    val state: String,
    val userId: Int,
    val userFrom: String,
    val createdOn: String?,
    val phoneContact: String?
)