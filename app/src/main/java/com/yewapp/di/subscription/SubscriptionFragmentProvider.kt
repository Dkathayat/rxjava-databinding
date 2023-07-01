package com.yewapp.di.subscription

import com.yewapp.ui.modules.subscription.fragment.CurrentPlanFragment
import com.yewapp.ui.modules.subscription.fragment.PricePlanFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SubscriptionFragmentProvider {

    @ContributesAndroidInjector
    abstract fun provideCurrentPlanFragment(): CurrentPlanFragment

    @ContributesAndroidInjector
    abstract fun providePricePlanFragment(): PricePlanFragment
}