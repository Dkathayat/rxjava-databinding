package com.yewapp.ui.modules.follower

import android.os.Bundle

class FollowerExtras {
    companion object {
        const val NAVIGATE_POSITION = "navigate_position"

        inline fun followerExtras(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var position = 0


        fun build(): Bundle {
            return Bundle().apply {
                putInt(NAVIGATE_POSITION, position)
            }
        }
    }
}