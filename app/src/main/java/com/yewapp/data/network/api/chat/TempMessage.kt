package com.yewapp.data.network.api.chat


data class TempMessage(
    val channelID: String?,
    val message: String?,
    val senderID: Int?,
    val sender: MessageSender?,
    val messageId: String?,
    val isUpdated: Boolean?,
    val timeToken: Long?
)

