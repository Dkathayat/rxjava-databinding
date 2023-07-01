package com.yewapp.ui.modules.verifyCode.extras

import android.os.Bundle

enum class VERIFY(val type: Int) {
    FROM_SIGN_UP(1),
    FROM_RESET_PASSWORD(2)
}

class VerifyCodeExtras {

    companion object {
        const val EMAIL = "email"
        const val FROM = "from"

        inline fun verifyCodeExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var email = ""
        var from = VERIFY.FROM_SIGN_UP.type
        fun build(): Bundle {
            return Bundle().apply {
                putString(EMAIL, email)
                putInt(FROM, from)
            }
        }
    }
}