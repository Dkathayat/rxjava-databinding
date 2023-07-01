package com.yewapp.ui.modules.addmodelandequipments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.addassociatememberdetails.fragment.profiledetails.AssociateProfileDetailsFragment
import com.yewapp.ui.modules.addassociatememberdetails.fragment.sportsimages.AssociateSportsImagesFragment
import com.yewapp.ui.modules.addmodelandequipments.fragments.addequipments.AddEquipmentsFragment
import com.yewapp.ui.modules.addmodelandequipments.fragments.addmodel.AddModelFragment

class AddModelEquipmentDetailsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val extras: Bundle?) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddModelFragment.newInstance(extras)
            1 -> AddEquipmentsFragment.newInstance(extras)
            else -> throw IllegalStateException()
        }
    }
}