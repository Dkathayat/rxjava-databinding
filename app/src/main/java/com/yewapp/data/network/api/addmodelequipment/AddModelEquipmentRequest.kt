package com.yewapp.data.network.api.addmodelequipment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AddModelEquipmentRequest(
    val associateId: String,
    val profileImage: String,
    val coverImage: String,
    val subSport: List<SubSport>,
    val equipmentData: List<EquipmentData>
)
//data class AddModelEquipmentRequest(
//    val associateId: String,
//    val profileImage: String,
//    val coverImage: String,
//    val subSport: List<SubSport>,
//    val equipmentData: List<EquipmentData>
//)

@Parcelize
data class EquipmentData(
    val brandMakeId: String,
    val brandName: String,
    val equipmentType: String,
    val frame: String,
    val modelId: String,
    val modelName: String,
    val other: String,
    val sportId: String,
    val subSportId: String,
    val subSportName: String,
    val type: String,
    val wheelSize: String,
    val year: String,
    @Transient
    var isSelected: Boolean = false
): Parcelable

@Parcelize
data class SubSport(
    val gradeLevel: String,
    val sportId: String,
    val subSportId: String,
    val subSportName: String
):Parcelable



//
//data class EquipmentData(
//    val brandMakeId: String,
//    val equipmentType: String,//helmet ot other equipemnet
//    val frame: String,
//    val modelId: String,
//    val other: String,
//    val sportId: String,
//    val subSportId: String,
//    val type: String,//model or equipment
//    val wheelSize: String,
//    val year: String,
//    @Transient
//    var isSelected:Boolean=false
//)
//
//data class SubSport(
//    val gradeLevel: String,
//    val sportId: String,
//    val subSportId: String
//)
//


//
//data class AddModelEquipmentRequest(
//    val subSport: List<SubSport>,
//    val equipmentData: List<EquipmentData>
//)
//
//data class EquipmentData(
//    val associateId: String,
//    val brandMakeId: String,
//    val equipmentType: String,
//    val frame: String,
//    val gradeLevel: String,
//    val modelId: String,
//    val other: String,
//    val sportId: String,
//    val subSportId: String,
//    val type: String,
//    val wheelSize: String,
//    val year: String
//)
//
//data class SubSport(
//    val associateId: String,
//    val gradeLevel: String,
//    val sportId: String,
//    val subSportId: String,
//    val type: String
//)