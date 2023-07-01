package com.yewapp.di.profile

import com.yewapp.ui.modules.editProfile.fragment.ProfileDetailFragment
import com.yewapp.ui.modules.editProfile.fragment.SportDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileFragmentProvider {

    @ContributesAndroidInjector
    abstract fun provideProfileDetailFragment(): ProfileDetailFragment

    @ContributesAndroidInjector
    abstract fun provideSportDetailFragment(): SportDetailFragment


}