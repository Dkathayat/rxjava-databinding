package com.yewapp.ui.modules.emailchange.extras

import android.os.Bundle

class EmailPhoneChangeExtras {

    companion object {
        const val IS_EMAIL_CHANGE = "email_change"
        const val IS_PHONE_CHANGE = "phone_change"

        inline fun emailPhoneChangeExtras(block: Builder.() -> Unit) =
            Builder().apply(block).build()
    }

    class Builder {
        var isEmailChange = true
        var isPhoneChange = true

        fun build(): Bundle {
            return Bundle().apply {
                putBoolean(IS_EMAIL_CHANGE, isEmailChange)
                putBoolean(IS_PHONE_CHANGE, isPhoneChange)
            }
        }
    }
}