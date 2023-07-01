package com.yewapp.data.network.api.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class ChatMessageModels()



@Parcelize
data class MessageSender(
    val senderId: Int?,
    val name: String?,
    val image: String?
): Parcelable



//Message Notification Object
data class MessageNotification(
    val channelId: String,
    val user: SpaceParticipant
)

@Parcelize
data class SpaceParticipant(
    val id: Int,
    val name: String,
    val image: String?,
    var lastReadMessageTimeToken: String?
): Parcelable


@Parcelize
data class LastMessage(
    var text: String,
    var type: String?,
    var timeToken: Long?
): Parcelable


@Parcelize
data class SpaceMetaData(
    val type: String,
    val spaceImage: String?,
    var participants: MutableList<SpaceParticipant>,
    var lastMessage: LastMessage?
): Parcelable

@Parcelize
data class SpaceData(
    val data: String
): Parcelable
