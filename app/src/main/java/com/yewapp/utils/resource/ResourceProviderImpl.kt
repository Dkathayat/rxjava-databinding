package com.yewapp.utils.resource

import android.content.Context
import androidx.core.content.ContextCompat
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(val context: Context) : ResourceProvider {
    override fun getString(stringRes: Int): String {
        return context.getString(stringRes)
    }

    override fun getString(stringRes: Int, vararg args: Any): String {
        return context.getString(stringRes, *args)
    }

    override fun getBoolean(boolRes: Int): Boolean {
        return context.resources.getBoolean(boolRes)
    }

    override fun getColor(colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    override fun getStringArray(stringRes: Int): Array<out String> {
        return context.resources.getStringArray(stringRes)
    }
}