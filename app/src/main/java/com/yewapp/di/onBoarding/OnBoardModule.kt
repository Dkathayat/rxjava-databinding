package com.yewapp.di.onBoarding

import com.yewapp.R
import com.yewapp.ui.modules.onboarding.OnBoardingAdapter
import dagger.Module
import dagger.Provides

@Module
class OnBoardModule {
    companion object {
        @JvmStatic
        @Provides
        fun providePagerAdapter(): OnBoardingAdapter = OnBoardingAdapter(
            arrayOf(
                R.layout.layout_onboard_1,
                R.layout.layout_onboard_2,
                R.layout.layout_onboard_3
            )
        )
    }
}