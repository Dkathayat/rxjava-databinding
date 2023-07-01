package com.yewapp.utils

import com.squareup.moshi.Moshi

val moshi: Moshi = Moshi.Builder().build()

inline fun <reified T> String?.fromJson(): T? {
    this ?: return null
    return try {
        val adapter = moshi.adapter<T>(T::class.java)
        adapter.fromJson(this)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> T?.toJson(): String? {
    this ?: return null
    return try {
        val adapter = moshi.adapter<T>(T::class.java)
        adapter.toJson(this)
    } catch (e: Exception) {
        null
    }
}