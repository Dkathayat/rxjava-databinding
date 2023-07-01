package com.yewapp.data.network.api.chat

import android.content.Context
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.yewapp.MainApplication
import com.yewapp.data.local.PreferenceKeys
import com.yewapp.data.network.api.signup.Profile
import com.yewapp.utils.PREF_NAME
import com.yewapp.utils.fromJson
import com.yewapp.utils.pubnub.Helper
import com.yewapp.utils.toJson


var TIMESTAMP_DIVIDER = 10_000L


class ChatMessage(builder: Builder) {

    //    @SerializedName("channelID")
//    @Expose
    var channelID: String? = null

    //    @SerializedName("sender")
//    @Expose
    var sender: MessageSender? = null

    //    @SerializedName("senderID")
//    @Expose
    var senderID: Int? = 0

    //
//    @SerializedName("message")
//    @Expose
    var message: String? = null

    //    @SerializedName("messageId")
//    @Expose
    var messageId: String? = null

    //
//    @SerializedName("isUpdated")
//    @Expose
    var isUpdated: Boolean = false

    @Transient
    private var messageViewType = 0

    @Transient
    private var ownMessage = false

    @Transient
    var timetoken: Long = 0

    private var user: Profile? = null

    @Transient
    lateinit var worker: Profile


    @Transient
    private var timestamp: String? = null

    @Transient
    private var key: Long? = null


    init {
        senderID = builder.senderID
        message = builder.message
        timetoken = builder.timeToken
        user = builder.user
        sender = builder.sender
        channelID = builder.channelID
        messageId = builder.messageId ?: messageId
        isUpdated = builder.isUpdated ?: isUpdated

        val worker = builder.user
        if (worker != null) {
            this.worker = worker
        }
        initializeCustomProperties()
    }


    fun initializeCustomProperties() {
        val data = MainApplication.mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val userJson = data.getString(PreferenceKeys.USER_DATA, "") ?: return
        val user = userJson.fromJson<Profile>() ?: return
//        val userId = getUser(user)
        val userId = user.userId
        ownMessage = userId == senderID
        timestamp = Helper.parseTime(timetoken / TIMESTAMP_DIVIDER)
        this.user = user
        key = Helper.trimTime(timetoken / TIMESTAMP_DIVIDER)
//        messageViewType = if (ownMessage) USER_VIEW else ASSOCIATE_VIEW
    }


    companion object Builder {
        private var channelID: String? = null
        private var message: String? = null
        private var timeToken: Long = 0
        private var sender: MessageSender? = null
        private var senderID: Int? = 0
        private var messageId: String? = null
        private var user: Profile? = null
        private var isUpdated: Boolean = false


        fun text(message: String?): Builder {
            this.message = message
            return this
        }

        fun timetoken(timeToken: Long): Builder {
            this.timeToken = timeToken
            return this
        }

        fun setUser(user: Profile): Builder {
            this.user = user
            this.senderID = user.userId
            return this
        }

        fun setSender(user: Profile): Builder {
            val imageUrl = user.profileImage
            this.sender = MessageSender(
                user.userId, user.firstName, imageUrl ?: ""
            )
            return this
        }

        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }


        fun setChannelId(channelId: String?): Builder {
            this.channelID = channelId
            return this
        }

        fun setMessageId(id: String): Builder {
            this.messageId = id
            return this
        }

        fun setIsSender(isUpdated: Boolean): Builder {
            this.isUpdated = isUpdated
            return this
        }

        fun build(): JsonObject? {
            return ChatMessage(this).generate()
        }

        fun newBuilder(): ChatMessage.Builder? {
            return Builder
        }

    }

    private fun generate(): JsonObject? {
        val payload = JsonObject()
        val sender = JsonParser.parseString(sender.toJson())
        payload.addProperty("type", "text")
        payload.addProperty("message", message)
        payload.add("sender", sender)
        payload.addProperty("timeToken", timeToken)
        payload.addProperty("channelID", channelID)
        payload.addProperty("isUpdated", isUpdated)
        payload.addProperty("senderID", senderID)
        payload.addProperty("id", messageId)
        return payload
    }


//    override fun getMessageViewType(): Int {
//        return messageViewType
//
//    }


//    var channelID: String? = null
//
//    @SerializedName("sender")
//    @Expose
//    var sender: MessageSender? = null
//    var senderID: Int = 0
//    var message: String? = null
//    var messageId: String? = null
//
//    @SerializedName("isUpdated")
//    @Expose
//    var isSent: Boolean = false
//
//
//    @Transient
//    private var ownMessage = false
//
//    @Transient
//    private var timestamp: String? = null
//
//    @Transient
//    private var key: Long? = null
//
//    @Transient
//    var timetoken: Long = 0
//
//    private var user: Profile? = null
//
//    @Transient
//    var worker: Profile? = null
//
//    @Transient
//    private var messageViewType = 0
//
//
//    init {
//        senderID = builder.senderId
//        message = builder.message
//        timetoken = builder.timeToken
//        user = builder.user
//        sender = builder.sender
//        channelID = builder.channelId
//        messageId = builder.messageId ?: messageId
//
//        val worker = builder.user
//        if (worker != null) {
//            this.worker = worker
//        }
//        initializeCustomProperties()
//    }
//
//
//    fun initializeCustomProperties() {
//        val data = MainApplication.mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//
//        val userJson = data.getString(PreferenceKeys.USER_DATA, "") ?: return
//        val user = userJson.fromJson<Profile>() ?: return
////        val userId = getUser(user)
//        val userId = user.userId
//        ownMessage = userId == senderID
//        timestamp = Helper.parseTime(timetoken / TIMESTAMP_DIVIDER)
//        this.user = user
//        key = Helper.trimTime(timetoken / TIMESTAMP_DIVIDER)
//        messageViewType = if (ownMessage) USER_VIEW else ASSOCIATE_VIEW
//    }
//
//
//    companion object Builder {
//        private var message: String? = null
//        private var timeToken: Long = 0
//        private var user: Profile? = null
//        private var senderId: Int = 0
//        private var sender: MessageSender? = null
//        private var channelId: String? = null
//        private var messageId: String? = null
//        private var messageViewType: Int? = null
//
//        fun message(message: String?): Builder {
//            this.message = message
//            return this
//        }
//
//        fun timetoken(timeToken: Long): Builder {
//            this.timeToken = timeToken
//            return this
//        }
//
//        fun setUser(user: Profile): Builder {
//            this.user = user
//            this.senderId = user.userId!!
//            return this
//        }
//
//        fun setSender(user: Profile): Builder {
//            val imageUrl = user.profileImage
//            this.sender = MessageSender(
//                user.userId!!, user.firstName!!, imageUrl ?: ""
//            )
//
//            return this
//        }
//
//        fun setChannelId(channelId: String?): Builder {
//            this.channelId = channelId
//            return this
//        }
//
//        fun setMessageId(messageId: String): Builder {
//            this.messageId = messageId
//            return this
//        }
//
//        fun setMessageViewType(messageViewType: Int): Builder {
//            this.messageViewType = messageViewType
//            return this
//        }
//
//
//        fun build(): JsonObject? {
//            return ChatMessage(this).generate()
//        }
//
//        fun newBuilder(): ChatMessage.Builder? {
//            return Builder
//        }
//
//    }
//
//    private fun generate(): JsonObject? {
//        val payload = JsonObject()
//
//        val sender = JsonParser.parseString(sender.toJson())
//        payload.addProperty("channelID", channelID)
//        payload.addProperty("messageId", messageId)
//        payload.addProperty("message", message)
//        payload.add("sender", sender)
//        payload.addProperty("isUpdated", isSent)
//        payload.addProperty("senderID", senderID)
////        payload.addProperty("type", "text")
//
//
//        return payload
//    }
//
//    override fun getMessageViewType(): Int {
//        return messageViewType
//    }


}