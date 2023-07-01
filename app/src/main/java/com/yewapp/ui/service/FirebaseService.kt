package com.yewapp.ui.service

/**
 * @author: Muheeb Mehraj
 * @description:
 */

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yewapp.data.local.PreferenceKeys.Companion.FIREBASE_TOKEN
import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.network.DataManager
import com.yewapp.utils.PREF_NAME

class FirebaseService : FirebaseMessagingService() {

    /**
     * The function is called when the notification is sent from the Firebase to the User's device.
     * When the notification is sent and the App is in Foreground this method will be called every-
     * time, but if it the App is in background then this will be called only if the Notification
     * only contains the "data" object and not the "notification" one.
     */
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

    }

    //Generates token on first time installation
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.i(TAG, "onNewToken: $p0")
        getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE).edit().putString(FIREBASE_TOKEN,p0).apply()

    }
}