package com.yewapp.ui.modules.forgotPassword

import com.yewapp.R
import com.yewapp.databinding.ActivityForgotPasswordBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.forgotPassword.navigators.ForgotPasswordNavigator
import com.yewapp.ui.modules.forgotPassword.vm.ForgotPasswordViewModel

class ForgotPasswordActivity :
    BaseActivity<ForgotPasswordNavigator, ForgotPasswordViewModel, ActivityForgotPasswordBinding>(),
    ForgotPasswordNavigator {

    lateinit var fragmentPagerAdapter: FragmentPagerAdapter

    override fun getLayout(): Int = R.layout.activity_forgot_password

    override fun init() {
        bind(ForgotPasswordViewModel::class.java)

    }

    override fun onViewModelCreated(viewModel: ForgotPasswordViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityForgotPasswordBinding) {
        fragmentPagerAdapter =
            FragmentPagerAdapter({ onChangePageClick() }, supportFragmentManager, lifecycle)
        viewDataBinding.viewPager.adapter = fragmentPagerAdapter
        viewDataBinding.viewPager.isUserInputEnabled = false
        viewDataBinding.viewPager.setCurrentItem(0, true)
    }

    private fun onChangePageClick() {
        var currentItem = viewDataBinding.viewPager.currentItem
        if (currentItem >= FRAGMENT_COUNT - 1) {
            navigateToPasswordSuccess()
        }
        viewDataBinding.viewPager.setCurrentItem(++currentItem, true)
        viewModel.progressCounter.set(currentItem + 1)
        viewModel.updateStepLabel(currentItem)
    }

    private fun navigateToPasswordSuccess() {
        startActivity(
            intentProviderFactory.create(
                ChangePasswordSuccessActivity::class.java,
                null,
                0
            )
        )
    }

    override fun onBackPress() {
        var currentItem = viewDataBinding.viewPager.currentItem
        if (currentItem == 0) {
            onBackPressedDispatcher.onBackPressed()
            //            super.onBackPressed()
            return
        }
        viewDataBinding.viewPager.setCurrentItem(--currentItem, true)
        viewModel.progressCounter.set(currentItem + 1)
        viewModel.updateStepLabel(currentItem)
    }

    override fun onBackPressed() {
        onBackPress()
    }
}