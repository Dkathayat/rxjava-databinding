package com.yewapp.ui.modules.emailphonechangeverifyotp.extras

import android.os.Bundle

class EmailPhoneChangeVerifyCodeExtras {

    companion object {
        const val EMAIL = "email"
        const val MOBILE = "mobile"
        const val COUNTRY_CODE = "country_code"
        const val IS_EMAIL_CHANGE = "email_change"
        const val IS_PHONE_CHANGE = "phone_change"

        inline fun emailPhoneChangeVerifyCodeExtras(block: Builder.() -> Unit) =
            Builder().apply(block).build()
    }

    class Builder {
        var isEmailChange = true
        var isPhoneChange = true
        var email = ""
        var mobile = ""
        var countrycode = ""

        fun build(): Bundle {
            return Bundle().apply {
                putBoolean(IS_EMAIL_CHANGE, isEmailChange)
                putBoolean(IS_PHONE_CHANGE, isPhoneChange)
                putString(EMAIL, email)
                putString(MOBILE, mobile)
                putString(COUNTRY_CODE, countrycode)
            }
        }
    }
}