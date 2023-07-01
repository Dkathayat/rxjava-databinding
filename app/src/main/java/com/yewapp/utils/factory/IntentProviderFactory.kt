package com.yewapp.utils.factory

import android.content.Context
import android.content.Intent
import android.os.Bundle

class IntentProviderFactory(var context: Context) {

    fun create(cls: Class<*>, extras: Bundle?, flags: Int): Intent {
        val intent = Intent(context, cls)
        if (extras != null) {
            intent.putExtras(extras)
        }
        intent.flags = flags
        return intent
    }

    fun create(action: String, extras: Bundle?, flags: Int): Intent {
        val intent = Intent(action)
        if (extras != null) {
            intent.putExtras(extras)
        }

        intent.flags = flags
        return intent
    }

}