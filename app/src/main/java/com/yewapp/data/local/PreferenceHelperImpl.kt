package com.yewapp.data.local

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.google.gson.Gson
import com.yewapp.di.PrefName
import javax.inject.Inject

class PreferencesHelperImpl @Inject constructor(
    var context: Context, @PrefName var prefName: String, var gson: Gson
) : PreferencesHelper {


    private fun getPrivateSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    override fun saveString(key: String, value: String) {
        getPrivateSharedPreference().edit()
            .putString(key, value)
            .apply()
    }

    override fun getString(key: String, defaultValue: String): String? {
        return getPrivateSharedPreference().getString(key, defaultValue)
    }

    override fun saveInt(key: String, value: Int) {
        getPrivateSharedPreference().edit()
            .putInt(key, value)
            .apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return getPrivateSharedPreference().getInt(key, defaultValue)
    }

    override fun saveFloat(key: String, value: Float) {
        getPrivateSharedPreference().edit()
            .putFloat(key, value)
            .apply()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return getPrivateSharedPreference().getFloat(key, defaultValue)
    }

    override fun saveLong(key: String, value: Long) {
        getPrivateSharedPreference().edit()
            .putLong(key, value)
            .apply()
    }

    override fun getLong(key: String, defaultValue: Long): Float {
        return getPrivateSharedPreference().getLong(key, defaultValue).toFloat()
    }

    override fun saveBoolean(key: String, value: Boolean) {
        getPrivateSharedPreference().edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return getPrivateSharedPreference().getBoolean(key, defaultValue)
    }

    override fun <T> saveObject(key: String, obj: T) {
        saveString(key, if (obj != null) gson.toJson(obj) else return)
    }

    override fun <T> getObject(key: String, type: Class<T>): T {
        val objJson = getString(key, "")
        return if (objJson != null) {
            gson.fromJson(objJson, type)
        } else throw Resources.NotFoundException()
    }
}