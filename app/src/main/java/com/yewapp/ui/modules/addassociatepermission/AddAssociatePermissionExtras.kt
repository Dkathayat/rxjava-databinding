package com.yewapp.ui.modules.addassociatepermission

import android.os.Bundle

class AddAssociatePermissionExtras {

    companion object {
        const val ASSOCIATE_ID = "associate_id"
        const val IS_UPDATE = "isUpdate"

        inline fun associateIDExtras(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var associateId = ""
        var isUpdate = false

        fun build(): Bundle {
            return Bundle().apply {
                putString(ASSOCIATE_ID, associateId)
                putBoolean(IS_UPDATE, isUpdate)
            }
        }
    }
}
