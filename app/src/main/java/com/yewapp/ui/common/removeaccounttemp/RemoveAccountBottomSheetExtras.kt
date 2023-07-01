package com.yewapp.ui.common.removeaccounttemp

import android.os.Bundle

class RemoveAccountBottomSheetExtras(message: String) {
    private constructor(builder: RemoveAccountBottomSheetExtras.Builder) : this(builder.message)

    companion object {
        const val REMOVE_ACCOUNT_MESSAGE = "message"

        //        const val POSITION = "index"
//        const val COMMENT_COUNT = "commentCount"
        inline fun removeAccountExtras(block: RemoveAccountBottomSheetExtras.Builder.() -> Unit) =
            RemoveAccountBottomSheetExtras.Builder()
                .apply(block)
                .build()
    }


    class Builder {
        var message = ""

        fun build(): Bundle {
            return Bundle().apply {

                putString(RemoveAccountBottomSheetExtras.REMOVE_ACCOUNT_MESSAGE, message)
            }
        }
    }

}