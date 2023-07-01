package com.yewapp.data.network

data class GenericResponse<T>(
    var code: Int,
    var message: String,
    var data: T
)



