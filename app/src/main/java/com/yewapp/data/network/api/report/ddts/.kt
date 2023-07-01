package com.yewapp.data.network.api.report.ddts

data class Jataka(
    val createdAt: Int,
    val createdBy: CreatedBy,
    val description: String,
    val files: List<File>,
    val id: Int,
    val sportActivityIcon: String,
    val status: String,
    val title: String
)