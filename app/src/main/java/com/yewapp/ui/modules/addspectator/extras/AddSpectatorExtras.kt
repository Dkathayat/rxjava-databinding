package com.yewapp.ui.modules.addspectator.extras

import android.os.Bundle
import com.yewapp.data.network.api.associate.Associate

class AddSpectatorExtras {

    companion object {
//        const val SPECTATOR_LIMIT = "spectator_limit"
        const val SPECTATOR_COUNT = "spectator_count"

        inline fun addSpectatorExtras(block: Builder.() -> Unit) =
            Builder().apply(block).build()
    }

    class Builder {
//         var spectatorMaxLimit: Int=0
         var spectatorCount: Int=0

        fun build(): Bundle {
            return Bundle().apply {
//                putInt(SPECTATOR_LIMIT, spectatorMaxLimit)
                putInt(SPECTATOR_COUNT, spectatorCount)

            }
        }
    }
}