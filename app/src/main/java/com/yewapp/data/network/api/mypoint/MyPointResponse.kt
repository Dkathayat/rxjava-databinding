package com.yewapp.data.network.api.mypoint

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class MyPointResponse(
    @SerializedName("Biking")
    @Expose
    val biking: String,
    @SerializedName("Hiking")
    @Expose
    val hiking: String,
    @SerializedName("Running")
    @Expose
    val running: String,
    @SerializedName("total_points")
    @Expose
    val totalPoints: Int
)