package com.yewapp.ui.modules.viewMedia

import android.os.Bundle
import com.yewapp.data.network.api.feed.Files

class MediaListExtras() {


    companion object {
        const val LIST = "list"


        inline fun mediaListExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var list = arrayListOf<Files>()

        fun build(): Bundle {
            return Bundle().apply {
                putParcelableArrayList(LIST, list)

            }
        }
    }
}