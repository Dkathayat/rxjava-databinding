package com.yewapp.ui.modules.dashboard.fragment.feeds.extras

import android.os.Bundle
import com.yewapp.data.network.api.feed.Feed

class EditFeedExtra {

    companion object {
        const val EDIT_FEED_DATA = "edit_feed_data"

        inline fun editFeedExtra(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        lateinit var editFeed: Feed

        fun build(): Bundle {
            return Bundle().apply {
                putParcelable(EDIT_FEED_DATA, editFeed)
            }
        }
    }
}