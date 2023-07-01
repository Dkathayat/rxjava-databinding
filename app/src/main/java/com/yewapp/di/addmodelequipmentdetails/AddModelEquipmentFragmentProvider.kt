package com.yewapp.di.addmodelequipmentdetails

import com.yewapp.ui.modules.addassociatememberdetails.fragment.profiledetails.AssociateProfileDetailsFragment
import com.yewapp.ui.modules.addassociatememberdetails.fragment.sportsimages.AssociateSportsImagesFragment
import com.yewapp.ui.modules.addmodelandequipments.fragments.addequipments.AddEquipmentsFragment
import com.yewapp.ui.modules.addmodelandequipments.fragments.addmodel.AddModelFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddModelEquipmentFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideAddModelFragment(): AddModelFragment

    @ContributesAndroidInjector
    abstract fun provideAddEquipmentsFragment(): AddEquipmentsFragment

}