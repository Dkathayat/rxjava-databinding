package com.yewapp.ui.modules.comments

import android.os.Bundle

class CommentExtras() {

    companion object {
        const val FEED_ID = "feedId"

        inline fun commentExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var feedId = -1
        fun build(): Bundle {
            return Bundle().apply {
                putInt(FEED_ID, feedId)
            }
        }
    }
}