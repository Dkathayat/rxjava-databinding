package com.yewapp.ui.modules.videofeeds.extras

import android.os.Bundle

class VideoFeedUserExtras {

    companion object {
        const val USER_ID = "userId"
        const val FOLLOW_STATUS = "followStatus"
        const val USER_NAME = "userName"
        const val USER_IMAGE = "userImage"
        const val DATETIME = "dateTime"
        inline fun videoFeedUserExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var userId = 0
        var followStatus = true
        var userName = ""
        var userImage = ""
        var dateTime = ""
        fun build(): Bundle {
            return Bundle().apply {
                putInt(USER_ID, userId)
                putBoolean(FOLLOW_STATUS, followStatus)
                putString(USER_NAME, userName)
                putString(USER_IMAGE, userName)
                putString(DATETIME, dateTime)
            }
        }
    }
}