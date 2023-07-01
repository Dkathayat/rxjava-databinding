package com.yewapp.ui.modules.listner

import android.os.Bundle
import com.yewapp.data.network.api.challenges.ChallengeModel

class ChallengeExtrasEdit {
    companion object {
        //        const val CHALLENGE_DATA_LOCATION = "challenge_data_location"
        const val CHALLENGE_ID = "challenge_id_edit"


        inline fun challengeExtras(block: ChallengeExtrasEdit.Builder.() -> Unit) =
            ChallengeExtrasEdit.Builder()
                .apply(block)
                .build()
    }

    class Builder {
        //        lateinit var challengeModelLocationData: ChallengeModelLocationData
        var challengeID = 0

        fun build(): Bundle {
            return Bundle().apply {
                putInt(CHALLENGE_ID, challengeID)


            }
        }
    }

}