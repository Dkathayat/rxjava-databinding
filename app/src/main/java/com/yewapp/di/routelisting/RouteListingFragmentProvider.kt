package com.yewapp.di.routelisting

import com.yewapp.ui.modules.addchallenge.challengeroutes.favorites.FavoritesRoutesFragment
import com.yewapp.ui.modules.addchallenge.challengeroutes.latest.LatestRoutesFragment
import com.yewapp.ui.modules.addchallenge.challengeroutes.popular.PopularRoutesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class RouteListingFragmentProvider {

    @ContributesAndroidInjector
    abstract fun providePopularRoutesFragment(): PopularRoutesFragment

    @ContributesAndroidInjector
    abstract fun provideLatestRoutesFragment(): LatestRoutesFragment

    @ContributesAndroidInjector
    abstract fun provideFavoritesRoutesFragment(): FavoritesRoutesFragment
}