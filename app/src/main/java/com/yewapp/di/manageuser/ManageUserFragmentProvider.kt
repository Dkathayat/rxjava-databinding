package com.yewapp.di.manageuser

import com.yewapp.ui.modules.manageuser.fragment.BlockedFragment
import com.yewapp.ui.modules.manageuser.fragment.FavoriteManageUserFragment
import com.yewapp.ui.modules.manageuser.fragment.MutedFragment
import com.yewapp.ui.modules.manageuser.fragment.ReportedManageUserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageUserFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideBlockedFragment(): BlockedFragment

    @ContributesAndroidInjector
    abstract fun provideMutedFragment(): MutedFragment

    @ContributesAndroidInjector
    abstract fun provideReportedManageUserFragment(): ReportedManageUserFragment

    @ContributesAndroidInjector
    abstract fun provideFavoriteManageUserFragment(): FavoriteManageUserFragment
}