package com.yewapp.ui.modules.addassociatememberdetails.fragment.extras

import android.os.Bundle

class AddMemberDetailsExtras {

    companion object {
        const val Member_EMAIL = "member_email"
        const val Member_DOB = "member_dob"
        const val Member_PASSWORD = "member_password"
        inline fun memberExtras(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var email = ""
        var dob = ""
        var password = ""
        fun build(): Bundle {
            return Bundle().apply {
                putString(Member_EMAIL, email)
                putString(Member_DOB, dob)
                putString(Member_PASSWORD, password)
            }
        }
    }
}