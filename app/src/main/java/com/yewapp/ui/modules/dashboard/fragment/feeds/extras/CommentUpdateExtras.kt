package com.yewapp.ui.modules.dashboard.fragment.feeds.extras

import android.os.Bundle

class CommentUpdateExtras {
    companion object {
        const val FEED_ID = "feedId"
        const val POSITION = "index"
        const val COMMENT_COUNT = "commentCount"

        inline fun commentUpdateExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var feedId = -1
        var position = -1
        var commentCount = 0
        fun build(): Bundle {
            return Bundle().apply {
                putInt(FEED_ID, feedId)
                putInt(POSITION, position)
                putInt(COMMENT_COUNT, commentCount)
            }
        }
    }
}