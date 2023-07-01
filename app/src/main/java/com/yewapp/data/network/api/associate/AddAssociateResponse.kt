package com.yewapp.data.network.api.associate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AddAssociateResponse(
    @SerializedName("associate_id")
    @Expose
    val associateId: Int
)