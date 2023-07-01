package com.yewapp.ui.modules.about

import android.os.Bundle

class CmsExtras {

    companion object {
        const val LINK = "link"

        inline fun cmsExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        var link = ""
        fun build(): Bundle {
            return Bundle().apply {
                putString(LINK, link)
            }
        }
    }
}