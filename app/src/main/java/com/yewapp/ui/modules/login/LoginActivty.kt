package com.yewapp.ui.modules.login

import android.content.Context
import android.content.Intent
import com.yewapp.R
import com.yewapp.data.local.PreferenceKeys
import com.yewapp.databinding.ActivityLoginBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.ui.modules.forgotPassword.ForgotPasswordActivity
import com.yewapp.ui.modules.verifyCode.VerifyCodeActivity
import com.yewapp.ui.modules.verifyCode.extras.VERIFY
import com.yewapp.ui.modules.verifyCode.extras.VerifyCodeExtras
import com.yewapp.utils.PREF_NAME

class LoginActivity : BaseActivity<LoginNavigator, LoginViewModel, ActivityLoginBinding>(),
    LoginNavigator {
    override fun getLayout(): Int = R.layout.activity_login

    override fun init() {
        bind(LoginViewModel::class.java)

    }

    override fun onViewModelCreated(viewModel: LoginViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityLoginBinding) {
        val token = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
            PreferenceKeys.FIREBASE_TOKEN,"empty")
        if (token != null) {
            viewModel.deviceToken = token
        }

    }

    override fun navigateToForgotPassword() {
        startActivity(
            intentProviderFactory.create(
                ForgotPasswordActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToLogin() {
        startActivity(
            intentProviderFactory.create(
                DashboardActivity::class.java,
                null,
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        finish()
    }

    override fun navigateToVerifyCode() {
        startActivity(
            intentProviderFactory.create(
                VerifyCodeActivity::class.java,
                VerifyCodeExtras.verifyCodeExtras {
                    email = viewModel.email
                    from = VERIFY.FROM_SIGN_UP.type
                },
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
    }
}