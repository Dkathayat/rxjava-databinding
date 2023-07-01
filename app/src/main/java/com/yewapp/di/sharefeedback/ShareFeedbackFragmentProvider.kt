package com.yewapp.di.sharefeedback

import com.yewapp.ui.modules.feedback.fragment.BugFixesFragment
import com.yewapp.ui.modules.feedback.fragment.ShareFeedbackFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ShareFeedbackFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideShareFeedbackFragment(): ShareFeedbackFragment

    @ContributesAndroidInjector
    abstract fun provideBugFixFragment(): BugFixesFragment
}