package com.yewapp.data.network.api.addmodelequipment

data class GetSavedModelEquipmentResponse(
    val associateId: String,
    val coverImage: String,
    val equipmentData: List<EquipmentData>,
    val profileImage: String,
    val subSport: List<SubSport>
)
//
//data class EquipmentData(
//    val brandMakeId: String,
//    val brandName: String,
//    val equipmentType: String,
//    val frame: String,
//    val modelId: String,
//    val modelName: String,
//    val other: String,
//    val sportId: String,
//    val subSportId: String,
//    val subSportName: String,
//    val type: String,
//    val wheelSize: String,
//    val year: String
//)
//
//data class SubSport(
//    val gradeLevel: String,
//    val sportId: String,
//    val subSportId: String,
//    val subSportName: String
//)