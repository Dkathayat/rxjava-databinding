//package com.yewapp.ui.modules.profilesubsportstype
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.Lifecycle
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.yewapp.ui.modules.editProfile.fragment.ProfileDetailFragment
//import com.yewapp.ui.modules.editProfile.fragment.SportDetailFragment
//
//class ProfileSportsTypeAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
//    FragmentStateAdapter(fragmentManager, lifecycle) {
//    override fun getItemCount(): Int = 2
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            0 -> SportDetailFragment()
//            1 -> ProfileDetailFragment()
//            else -> throw IllegalStateException()
//        }
//    }
//}