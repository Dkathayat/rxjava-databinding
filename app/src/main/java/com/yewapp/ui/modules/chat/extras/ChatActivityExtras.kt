package com.yewapp.ui.modules.chat.extras

import android.os.Bundle
import com.yewapp.data.network.api.associate.Associate

class ChatActivityExtras {

    companion object {
        const val ASSOCIATE_DETAILS = "associate_details"
        const val IS_ASSOCIATE = "is_associate"
//        const val ASSOCIATE_PARENT_DETAILS = "associate_parent_details"

        //        const val Member_DOB = "member_dob"
//        const val Member_PASSWORD = "member_password"
        inline fun associateDetailsExtras(block: Builder.() -> Unit) =
            Builder().apply(block).build()
    }

    class Builder {
        lateinit var associateDetails: Associate
        var isAssociate = true

        //        lateinit var associateParentDetails: AssociateParent
        fun build(): Bundle {
            return Bundle().apply {
                putParcelable(ASSOCIATE_DETAILS, associateDetails)
                putBoolean(IS_ASSOCIATE, isAssociate)

            }
        }
    }
}