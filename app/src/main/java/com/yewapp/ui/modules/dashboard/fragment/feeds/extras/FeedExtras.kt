package com.yewapp.ui.modules.dashboard.fragment.feeds.extras

import android.os.Bundle

class FeedExtras() {

    companion object {
        const val FEED_ID = "feedId"

        inline fun feedExtras(block: Builder.() -> Unit) =
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