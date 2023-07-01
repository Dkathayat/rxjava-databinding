package com.yewapp.ui.modules.forgotPassword

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.forgotPassword.fragments.EnterPasswordFragment
import com.yewapp.ui.modules.forgotPassword.fragments.ResetPasswordFragment
import com.yewapp.ui.modules.forgotPassword.fragments.VerifyCodeFragment

const val FRAGMENT_COUNT = 3

class FragmentPagerAdapter(
    val listener: () -> Unit,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = FRAGMENT_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ResetPasswordFragment(listener)
            1 -> VerifyCodeFragment(listener)
            2 -> EnterPasswordFragment(listener)
            else -> throw IllegalArgumentException()
        }
    }
}