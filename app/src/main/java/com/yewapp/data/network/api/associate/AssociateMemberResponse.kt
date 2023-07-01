package com.yewapp.data.network.api.associate

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class AssociateMemberResponse(
    val associateCount: Int,
    val associateList: List<Associate>,
    val maxAssociates: Int,
    val associateId: Int?

)

@Parcelize
data class Associate(
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


@Parcelize
data class AssociateParent(
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
    val status: String?,
    val userId: Int?
) : Parcelable

