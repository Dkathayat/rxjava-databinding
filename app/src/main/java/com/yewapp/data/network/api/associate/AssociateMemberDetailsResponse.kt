package com.yewapp.data.network.api.associate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AssociateMemberDetailsResponse(
    val address: String,
    @SerializedName("associate_relation")
    @Expose
    val associateRelation: String,
    val bio: String,
    @SerializedName("city_id")
    @Expose
    val cityId: String,
    val dob: String,
    val email: String,
    @SerializedName("first_name")
    @Expose
    val firstName: String,
    val gender: String,
    @SerializedName("heart_rate")
    @Expose
    val heartRate: String,
    @SerializedName("last_name")
    @Expose
    val lastName: String,
    @SerializedName("mobile_no")
    @Expose
    val mobileNo: String,
    val pincode: String,
    @SerializedName("profile_cover_image")
    @Expose
    val profileCoverImage: String,
    @SerializedName("profile_image")
    @Expose
    val profileImage: String,
    val status: String,
    val weight: String
)