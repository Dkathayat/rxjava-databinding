package com.yewapp.di.manageshortvideo

import com.yewapp.ui.modules.manageshortvideo.fragment.ReportedShortCommentFragment
import com.yewapp.ui.modules.manageshortvideo.fragment.ReportedShortVideoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageShortFragmentProvider {

    @ContributesAndroidInjector
    abstract fun provideReportedShortVideoFragment(): ReportedShortVideoFragment

    @ContributesAndroidInjector
    abstract fun provideReportedShortCommentFragment(): ReportedShortCommentFragment
}