package com.yewapp.ui.modules.addchallenge.challengepreview.extras

import android.os.Bundle
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.data.network.api.sports.Sport

class UpdateParticipantsExtras {


    companion object {

        const val PARTICIPANTS_LIST = "participants_list"

        inline fun participantsListExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {

        var updateParticipantsList = arrayListOf<InviteMember>()

        fun build(): Bundle {
            return Bundle().apply {
                putParcelableArrayList(PARTICIPANTS_LIST, updateParticipantsList)
            }
        }
    }
}