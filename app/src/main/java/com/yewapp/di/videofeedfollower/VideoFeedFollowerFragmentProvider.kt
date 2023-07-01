package com.yewapp.di.videofeedfollower

import com.yewapp.ui.modules.videofeeds.followers.VideoFeedFollowerFragment
import com.yewapp.ui.modules.videofeeds.following.VideoFeedFollowingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class VideoFeedFollowerFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideVideoFeedFollowerFragment(): VideoFeedFollowerFragment

    @ContributesAndroidInjector
    abstract fun provideVideoFeedFollowingFragment(): VideoFeedFollowingFragment
}