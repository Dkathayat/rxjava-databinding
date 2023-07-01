package com.yewapp.data.network.api.spectator

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Spectator(
    val age: String?,
    @SerializedName("created_on")
    @Expose
    val createdOn: String?,
    val email: String?,
    val location: String?,
    val name: String?,
    @SerializedName("profile_image")
    @Expose
    val profileImage: String?,
    val relation: String?,
    var status: String?,
    val userId: Int?
) : Parcelable
{
}