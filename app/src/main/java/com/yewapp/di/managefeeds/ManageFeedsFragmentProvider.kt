package com.yewapp.di.managefeeds

import com.yewapp.ui.modules.managefeeds.fragment.comment.CommentedFragment
import com.yewapp.ui.modules.managefeeds.fragment.reported.FeedReportedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ManageFeedsFragmentProvider {

    @ContributesAndroidInjector
    abstract fun provideReportedFragment(): FeedReportedFragment

    @ContributesAndroidInjector
    abstract fun provideCommentedFragment(): CommentedFragment
}