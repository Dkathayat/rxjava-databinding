package com.yewapp.di.follower

import com.yewapp.ui.modules.follower.fragments.MyFollowersFragment
import com.yewapp.ui.modules.follower.fragments.MyFollowingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FollowerFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideMyFollowersFragment(): MyFollowersFragment

    @ContributesAndroidInjector
    abstract fun provideMyFollowingFragment(): MyFollowingFragment
}