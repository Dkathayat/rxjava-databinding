package com.yewapp.ui.modules.forgotPassword

import com.yewapp.R
import com.yewapp.databinding.ActivityChangePasswordSuccessBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.forgotPassword.navigators.ChangePasswordSuccessNavigator
import com.yewapp.ui.modules.forgotPassword.vm.ChangePasswordSuccessViewModel
import com.yewapp.ui.modules.login.LoginActivity

class ChangePasswordSuccessActivity :
    BaseActivity<ChangePasswordSuccessNavigator, ChangePasswordSuccessViewModel, ActivityChangePasswordSuccessBinding>(),
    ChangePasswordSuccessNavigator {
    override fun getLayout(): Int = R.layout.activity_change_password_success

    override fun init() {
        bind(ChangePasswordSuccessViewModel::class.java)

    }

    override fun onViewModelCreated(viewModel: ChangePasswordSuccessViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChangePasswordSuccessBinding) {

    }

    override fun navigateToLogin() {
        startActivity(
            intentProviderFactory.create(
                LoginActivity::class.java,
                null,
                0
            )
        )
    }
}