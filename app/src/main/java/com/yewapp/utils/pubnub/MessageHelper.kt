package com.yewapp.utils.pubnub

import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.network.api.chat.ChatMessage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MessageHelper {

    private val HEADER_FULL = 10
    private val HEADER = 20
    private val MIDDLE = 30
    private val END = 40

    var preferencesHelper: PreferencesHelper? = null

    @Inject
    fun MessageHelper(preferencesHelper: PreferencesHelper?) {
        this.preferencesHelper = preferencesHelper
    }

    fun MessageHelper() {}

    fun chain(currentMsg: ChatMessage, previousMsg: ChatMessage) {
        val diffToPrev: Long = (currentMsg.timetoken - previousMsg.timetoken) / 10000L
        val offset = TimeUnit.MINUTES.toMillis(1)

//        LoginResponse worker = preferencesHelper.getUser(PreferenceKeys.Companion.getUSER_PREFERENCE_KEY());
//        val ownMessage = previousMsg.senderID === currentMsg.senderID
        var chainable = false
//        if (ownMessage) chainable = diffToPrev <= offset
//        if (ownMessage) {
//            if (chainable) {
//                currentMsg.(assignType(currentMsg, END))
//                if (isTypeOf(previousMsg, HEADER_FULL)) {
//                    previousMsg.setMessageViewType(assignType(previousMsg, HEADER))
//                } else if (isTypeOf(previousMsg, END)) {
//                    previousMsg.setMessageViewType(assignType(previousMsg, MIDDLE))
//                }
//            } else {
//                currentMsg.setMessageViewType(assignType(currentMsg, HEADER_FULL))
//                if (!isTypeOf(previousMsg, HEADER_FULL)) {
//                    previousMsg.setMessageViewType(assignType(previousMsg, END))
//                }
//            }
//        } else {
//            currentMsg.setMessageViewType(assignType(currentMsg, HEADER_FULL))
//        }
    }

//    private fun isTypeOf(instance: Message, type: Int): Boolean {
//        if (type == HEADER_FULL) {
//            return instance.getMessageViewType() === ChatItem.Companion.getTYPE_OWN_HEADER_FULL() || instance.getMessageViewType() === ChatItem.Companion.getTYPE_REC_HEADER_FULL()
//        }
//        if (type == HEADER) {
//            return instance.getMessageViewType() === ChatItem.Companion.getTYPE_OWN_HEADER_SERIES() || instance.getMessageViewType() === ChatItem.Companion.getTYPE_REC_HEADER_SERIES()
//        }
//        if (type == MIDDLE) {
//            return instance.getMessageViewType() === ChatItem.Companion.getTYPE_OWN_MIDDLE() || instance.getMessageViewType() === ChatItem.Companion.getTYPE_REC_MIDDLE()
//        }
//        return if (type == END) {
//            instance.getMessageViewType() === ChatItem.Companion.getTYPE_OWN_END() || instance.getMessageViewType() === ChatItem.Companion.getTYPE_REC_END()
//        } else false
//    }

//    private fun assignType(instance: ChatMessage, type: Int): Int {
//        if (type == HEADER_FULL) {
//            return if (instance.isOwnMessage()) ChatItem.Companion.getTYPE_OWN_HEADER_FULL() else ChatItem.Companion.getTYPE_REC_HEADER_FULL()
//        }
//        if (type == HEADER) {
//            return if (instance.isOwnMessage()) ChatItem.Companion.getTYPE_OWN_HEADER_SERIES() else ChatItem.Companion.getTYPE_REC_HEADER_SERIES()
//        }
//        if (type == MIDDLE) {
//            return if (instance.isOwnMessage()) ChatItem.Companion.getTYPE_OWN_MIDDLE() else ChatItem.Companion.getTYPE_REC_MIDDLE()
//        }
//        return if (type == END) {
//            if (instance.isOwnMessage()) ChatItem.Companion.getTYPE_OWN_END() else ChatItem.Companion.getTYPE_REC_END()
//        } else -1
//    }
}