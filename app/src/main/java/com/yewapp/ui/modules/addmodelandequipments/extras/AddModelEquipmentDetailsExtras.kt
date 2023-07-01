package com.yewapp.ui.modules.addmodelandequipments.extras

import android.os.Bundle
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.data.network.api.sports.Sport

class AddModelEquipmentDetailsExtras {

    companion object {
        const val IS_EDIT = "is_edit"
        const val ASSOCIATE_ID = "associate_id"
        const val PROFILE_IMAGE = "profile_image"
        const val PROFILE_COVER_IMAGE = "profile_cover_image"
        const val SELECTED_SPORTS_TYPE = "selected_sports_type"
        const val SELECTED_SUB_SPORTS = "selected_sub_sports"

        inline fun sportsAndSubSportsExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var isEdit = false
        var associateID = ""
        var profileImage = ""
        var profileCoverImage = ""
        lateinit var selectedSportType: SportType
        var selectedSubSports = arrayListOf<Sport>()

        fun build(): Bundle {
            return Bundle().apply {
                putBoolean(IS_EDIT, isEdit)
                putString(ASSOCIATE_ID, associateID)
                putString(PROFILE_IMAGE, profileImage)
                putString(PROFILE_COVER_IMAGE, profileCoverImage)
                putParcelable(SELECTED_SPORTS_TYPE, selectedSportType)
                putParcelableArrayList(SELECTED_SUB_SPORTS, selectedSubSports)
            }
        }
    }
}