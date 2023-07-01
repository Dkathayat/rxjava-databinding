package com.yewapp.di.dashboard

import com.yewapp.ui.common.removeaccounttemp.RemoveAccountBottomSheet
import com.yewapp.ui.modules.dashboard.fragment.challenges.ChallengesFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.active.ActiveFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.created.CreatedByMeFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.favorite.FavoriteFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.past.PastChallengeFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.upcoming.UpcomingFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.AllFeedsFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.FeedsFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.MyFeedsFragment
import com.yewapp.ui.modules.profile.MainProfileFragment
import com.yewapp.ui.modules.profile.fragment.activities.ActivitiesFragment
import com.yewapp.ui.modules.profile.fragment.associate.AssociateFragment

import com.yewapp.ui.modules.profile.fragment.mypoints.MyPointsFragment
import com.yewapp.ui.modules.profile.fragment.profile.ProfileFragment
import com.yewapp.ui.modules.profile.fragment.spectator.SpectatorFragment
import com.yewapp.ui.modules.refer.fragments.ChooseContactsFragment
import com.yewapp.ui.modules.refer.fragments.ReferRewardHistoryFragment
import com.yewapp.ui.modules.refer.fragments.ReferredFriendsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class DashboardFragmentProvider {

    @ContributesAndroidInjector
    abstract fun provideFeedsFragment(): FeedsFragment

    @ContributesAndroidInjector
    abstract fun provideChallengesFragment(): ChallengesFragment

    @ContributesAndroidInjector
    abstract fun provideMainProfileFragment(): MainProfileFragment

    @ContributesAndroidInjector
    abstract fun provideAllFeedsFragment(): AllFeedsFragment

    @ContributesAndroidInjector
    abstract fun provideMyFeedsFragment(): MyFeedsFragment

    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun provideActivitiesFragment(): ActivitiesFragment

    @ContributesAndroidInjector
    abstract fun providePointsFragment(): MyPointsFragment

    @ContributesAndroidInjector
    abstract fun provideAssociateFragment(): AssociateFragment

    @ContributesAndroidInjector
    abstract fun provideRemoveAccountBottomSheet(): RemoveAccountBottomSheet

    @ContributesAndroidInjector
    abstract fun provideActiveFragment(): ActiveFragment

//    @ContributesAndroidInjector
//    abstract fun provideActivitiesBottomSheet(): ActivitesFilterBottomSheet

    @ContributesAndroidInjector
    abstract fun provideCreatedByMeFragment(): CreatedByMeFragment

    @ContributesAndroidInjector
    abstract fun providePastChallengeFragment(): PastChallengeFragment

    @ContributesAndroidInjector
    abstract fun provideChooseContactsFragment(): ChooseContactsFragment

    @ContributesAndroidInjector
    abstract fun provideReferRewardHistoryFragment(): ReferRewardHistoryFragment

    @ContributesAndroidInjector
    abstract fun provideReferredFriendsFragment(): ReferredFriendsFragment

    @ContributesAndroidInjector
    abstract fun provideFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun provideUpcomingFragment(): UpcomingFragment

    @ContributesAndroidInjector
    abstract fun provideSpectatorFragment(): SpectatorFragment

}