package com.yewapp.di.addassociate

import com.yewapp.ui.modules.addassociatememberdetails.fragment.profiledetails.AssociateProfileDetailsFragment
import com.yewapp.ui.modules.addassociatememberdetails.fragment.sportsimages.AssociateSportsImagesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddAssociateFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideAssociateProfileDetailsFragment(): AssociateProfileDetailsFragment

    @ContributesAndroidInjector
    abstract fun provideAssociateSportsImagesFragment(): AssociateSportsImagesFragment
}