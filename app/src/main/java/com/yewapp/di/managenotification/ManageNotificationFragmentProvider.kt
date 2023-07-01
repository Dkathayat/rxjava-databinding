package com.yewapp.di.managenotification

import com.yewapp.ui.modules.managenotification.fragment.NotificationFragment
import com.yewapp.ui.modules.managenotification.fragment.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageNotificationFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun provideSettingsFragment(): SettingsFragment
}