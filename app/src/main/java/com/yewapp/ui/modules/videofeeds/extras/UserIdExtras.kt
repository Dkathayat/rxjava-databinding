package com.yewapp.ui.modules.videofeeds.extras

import android.os.Bundle

class UserIdExtras {
    companion object {
        const val USER_ID = "userId"
        const val USER_Name = "userName"
        inline fun userIdExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var userId = 0
        var userName = ""
        fun build(): Bundle {
            return Bundle().apply {
                putInt(USER_ID, userId)
                putString(USER_Name, userName)
            }
        }
    }

}