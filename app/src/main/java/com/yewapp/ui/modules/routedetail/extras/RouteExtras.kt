package com.yewapp.ui.modules.routedetail.extras

import android.os.Bundle

class RouteExtras() {

    companion object {
        //  const val ROUTE ="route"
        const val ID = "id"
        inline fun routeExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }

    class Builder {
        //        lateinit var route : Route
        var id: Int = 0
        fun build(): Bundle {
            return Bundle().apply {
                putInt(ID, id)
            }
        }
    }
}