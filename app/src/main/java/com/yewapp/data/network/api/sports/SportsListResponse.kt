package com.yewapp.data.network.api.sports

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Sport(
    val backgroundImage: String?,
    val description: String?,
    val gradeLevel: String?,
    val icon: String?,
    val id: String?,
    @SerializedName("modelList")
    @Expose
    val brandList: List<Brand>?,
    val name: String?,
    val profileType: String?,
    val sportId: String?,
    @Transient
    var isSelected: Boolean?, // this "isSelected" is added by krishna for layout use
) : Parcelable

@Parcelize
data class Brand(
    val brandId: String?,
    val brandName: String?,
    @SerializedName("modelArray")
    @Expose
    val modelList: List<Model>?,
    val subSportId: String?
) : Parcelable

@Parcelize
data class Model(
    val bId: String?,
    @SerializedName("frameSize")
    @Expose
    val frameList: List<FrameSize>?,
    val modelId: String?,
    val modelName: String?,
    @SerializedName("type")
    @Expose
    val typeList: List<Type>?,
    @SerializedName("wheelSize")
    @Expose
    val wheelSizeList: List<WheelSize>?
) : Parcelable

@Parcelize
data class FrameSize(
    val frameSize: String?,
    val id: Int?
) : Parcelable

@Parcelize
data class Type(
    val id: Int?,
    val type: String?
) : Parcelable

@Parcelize
data class WheelSize(
    val id: Int?,
    val wheelSize: String?
) : Parcelable


//
//@Parcelize
//data class Sport(
//    val backgroundImage: String?,
//    val description: String?,
//    val icon: String?,
//    val id: String?,
//    val modelList: List<Model>?,
//    val name: String?,
//    val profileType: String?,
//    val sportId: String?,
//    @Transient
//    var isSelected: Boolean?, // this "isSelected" is added by krishna for layout use
//) : Parcelable
//
//@Parcelize
//data class Model(
//    val brandId: String?,
//    val brandName: String?,
//    val modelId: String?,
//    val modelName: String?,
//    val year: String?,
//    val brand: List<Brand>?,
//) : Parcelable
//
//@Parcelize
//data class Brand(
//    val brand_name: String?,
//    val created_by: String?,
//    val id: Int?,
//    val status: String?,
//    val sub_sport_id: String?,
//    val updated_by: String?
//) : Parcelable
////
