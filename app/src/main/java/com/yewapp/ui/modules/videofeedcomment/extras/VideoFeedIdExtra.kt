package com.yewapp.ui.modules.videofeedcomment.extras

import android.os.Bundle

class VideoFeedIdExtra(feedId: Int) {
    private constructor(builder: VideoFeedIdExtra.Builder) : this(builder.feedId)

    companion object {
        const val FEED_ID = "feed_id"
        const val POSITION = "index"
        const val COMMENT_COUNT = "commentCount"
        inline fun videoFeedIdExtra(block: VideoFeedIdExtra.Builder.() -> Unit) =
            VideoFeedIdExtra.Builder()
                .apply(block)
                .build()
    }


    class Builder {
        var feedId = 0
        var position = 0
        var commentCount = 0
        fun build(): Bundle {
            return Bundle().apply {

                putInt(VideoFeedIdExtra.FEED_ID, feedId)
                putInt(POSITION, position)
                putInt(COMMENT_COUNT, commentCount)
            }
        }
    }

}