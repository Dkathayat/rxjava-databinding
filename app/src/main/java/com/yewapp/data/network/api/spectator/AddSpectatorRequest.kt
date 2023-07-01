package com.yewapp.data.network.api.spectator

data class AddSpectatorRequest(
    val contactName: List<String>,
    val contactNumber: List<String>,
    val spectatorMemberid: List<Int>,
    val usersFrom: String
)