package com.yewapp.ui.modules.createroute.extras

import android.os.Bundle
import com.yewapp.data.network.api.routes.Coordinate

class CreateRouteExtras {

    companion object {
        const val ROUTE_ID = "route_Id"
        const val PROFILE_TYPE = "profile_type"
        const val MAP_STYLE = "map_type"
        const val CREATE_EDIT_ROUTE_DATA = "create_edit_route"

        inline fun createEditRouteExtras(block: Builder.() -> Unit) =
            Builder()
                .apply(block)
                .build()
    }


    class Builder {
        //        lateinit var createEditRouteModel: CreateEditRouteModel
        var routeID = 0
        var profileType = ""
        var mapStyle = ""
        var coordinates = arrayListOf<Coordinate>()


        fun build(): Bundle {
            return Bundle().apply {
                putInt(ROUTE_ID, routeID)
                putString(PROFILE_TYPE, profileType)
                putString(MAP_STYLE, mapStyle)
                putParcelableArrayList(CREATE_EDIT_ROUTE_DATA, coordinates)
            }
        }
    }

}