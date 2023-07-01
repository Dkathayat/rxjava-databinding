package com.yewapp.di.chat

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.yewapp.BuildConfig
import com.yewapp.data.local.PreferenceKeys
import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.network.api.signup.Profile
import dagger.Module
import dagger.Provides


@Module
class ChatModule {

    /**
     * @author: Narbir Singh
     * @description: This module is used to create pubnub object
     */
    @Provides
    fun providesPubNubClient(preferencesHelper: PreferencesHelper): PubNub {
        val userDetails = preferencesHelper.getObject(PreferenceKeys.USER_DATA, Profile::class.java)
        val pnConfiguration = PNConfiguration(UserId("${userDetails.userId}")).apply {
            publishKey = BuildConfig.PUBLISH_KEY
            subscribeKey = BuildConfig.SUBSCRIBE_KEY
            logVerbosity = PNLogVerbosity.BODY
            reconnectionPolicy = PNReconnectionPolicy.LINEAR
            maximumReconnectionRetries = 10
        }
        return PubNub(pnConfiguration)
    }

}