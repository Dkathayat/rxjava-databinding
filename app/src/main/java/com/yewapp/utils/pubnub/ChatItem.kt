package com.yewapp.utils.pubnub


/**
 * Helper class used Group PubNub messages together if they arrive in a short span of time.
 */
abstract class ChatItem {
    companion object {
        val USER_VIEW = 1
        val ASSOCIATE_VIEW = 2


        //        val TYPE_OWN_HEADER_SERIES = 2
//        val TYPE_OWN_MIDDLE = 3
//        val TYPE_OWN_END = 4
//
//        val TYPE_REC_HEADER_FULL = 5
//        val TYPE_REC_HEADER_SERIES = 6
//        val TYPE_REC_MIDDLE = 7
//        val TYPE_REC_END = 8
    }

    abstract fun getMessageViewType(): Int
}