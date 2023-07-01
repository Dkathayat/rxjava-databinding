package com.yewapp.utils.resource

import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes stringRes: Int): String
    fun getString(@StringRes stringRes: Int, vararg args: Any): String
    fun getBoolean(@BoolRes boolRes: Int): Boolean
    fun getColor(@ColorRes colorRes: Int): Int
    fun getStringArray(@ArrayRes stringRes: Int): Array<out String>
}