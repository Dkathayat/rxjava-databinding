package com.yewapp.ui.modules.profilesubsportstype.extras

import android.os.Bundle
import com.yewapp.data.network.api.profile.SportType

class ProfileSubSportsTypeExtras {

    companion object {
        const val IS_EDIT = "is_edit"
        const val ASSOCIATE_ID = "associate_d"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_COVER_IMAGE = "profile_cover_image"
        const val SPORTS_TYPE_DATA = "sports_type_data"

        inline fun addSportsDataExtras(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var isEdit = false
        var associateID = ""
        var profileImage = ""
        var profileCoverImage = ""
        lateinit var sportType: SportType

        fun build(): Bundle {
            return Bundle().apply {
                putBoolean(IS_EDIT, isEdit)
                putString(ASSOCIATE_ID, associateID)
                putString(PROFILE_IMAGE, profileImage)
                putString(PROFILE_COVER_IMAGE, profileCoverImage)
                putParcelable(SPORTS_TYPE_DATA, sportType)
            }
        }
    }
}