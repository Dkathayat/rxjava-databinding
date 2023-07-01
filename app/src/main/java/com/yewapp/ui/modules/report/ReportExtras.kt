package com.yewapp.ui.modules.report

import android.os.Bundle

class ReportExtras {

    companion object {
        const val FEED_ID = "feedId"
        const val OPTION = "option"
        inline fun reportExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var feedId = 0
        var option = ""
        fun build(): Bundle {
            return Bundle().apply {
                putInt(FEED_ID, feedId)
                putString(OPTION, option)
            }
        }
    }
}