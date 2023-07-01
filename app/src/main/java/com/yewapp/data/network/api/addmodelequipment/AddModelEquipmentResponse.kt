package com.yewapp.data.network.api.addmodelequipment

//data class kjhk(
//    val code: Int,
//    val `data`: List<Data>,
//    val message: String
//)

data class AddModelEquipmentResponse(
    val associateId: Any,
    val brandMakeId: String,
    val equipmentType: String,
    val frame: String,
    val gradeLevel: String,
    val id: Int,
    val modelId: String,
    val other: String,
    val sportId: String,
    val subsportid: String,
    val type: String,
    val userId: String,
    val wheelSize: String,
    val year: String
)
