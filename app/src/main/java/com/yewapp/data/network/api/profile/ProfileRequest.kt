package com.yewapp.data.network.api.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileImageRequest(
    val associateId: String,
    val profileImage: String,
    val profileCoverImage: String
)
data class ProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val cityId: String,
    val address: String,

    @SerializedName("pincode")
    @Expose
    val pinCode: String,
    val gender: String,
    val dob: String,
    val weight: String,
    val heartRate: String,
    val bio: String,
    val profileImage: String,
    val profileCoverImage: String,
    val latitude: String,
    val longitude: String
)