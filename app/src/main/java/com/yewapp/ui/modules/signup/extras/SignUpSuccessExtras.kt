package com.yewapp.ui.modules.signup.extras

import android.os.Bundle

class SignUpSuccessExtras {

    companion object {
        const val EMAIL = "email"

        inline fun signUpSuccessExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var email = ""
        fun build(): Bundle {
            return Bundle().apply {
                putString(EMAIL, email)
            }
        }
    }
}