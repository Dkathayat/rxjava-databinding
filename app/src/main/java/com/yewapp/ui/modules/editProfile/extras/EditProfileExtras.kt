package com.yewapp.ui.modules.editProfile.extras

import android.os.Bundle

class EditProfileExtras {

    companion object {
        const val IS_SIGN_UP = "is_sign_up"
        const val ASSOCIATE_ID = "associate_id"

        inline fun editProfileExtras(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var assoicateID = ""
        var isSignUp = true

        fun build(): Bundle {
            return Bundle().apply {
                putString(ASSOCIATE_ID, assoicateID)
                putBoolean(IS_SIGN_UP, isSignUp)
            }
        }
    }
}