package com.yewapp.data.local

interface PreferencesHelper {
    fun saveString(key: String, value: String)

    fun getString(key: String, defaultValue: String): String?

    fun saveInt(key: String, value: Int)

    fun getInt(key: String, defaultValue: Int): Int

    fun saveFloat(key: String, value: Float)

    fun getFloat(key: String, defaultValue: Float): Float

    fun saveLong(key: String, value: Long)

    fun getLong(key: String, defaultValue: Long): Float

    fun saveBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun <T> saveObject(key: String, obj: T)

    fun <T> getObject(key: String, type: Class<T>): T
}

class PreferenceKeys {
    companion object {
        const val API_TOKEN: String = "access_token"
        const val AWS_CREDENTIAL: String = "access_credential"
        const val LOGIN_STATUS: String = "login_status"
        const val ON_BOARDING_STATUS = "on_boarding_status"
        const val USER_DATA = "user_data"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val USER_SUBSCRIPTION = "user_subscription"
        const val FIREBASE_TOKEN = "FCMTOKEN"
        const val CHALLENGE_CREATED_STATUS = "challenge_created_status"
    }
}