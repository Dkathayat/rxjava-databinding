package com.yewapp.ui.modules.splash

import android.annotation.SuppressLint
import com.yewapp.R
import com.yewapp.databinding.ActivitySplashBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.ui.modules.onboarding.OnBoardingActivity
import com.yewapp.ui.modules.signup.SignUpOptionsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class SplashActivity : BaseActivity<SplashNavigator, SplashViewModel, ActivitySplashBinding>(),
    SplashNavigator {
    override fun getLayout(): Int = R.layout.activity_splash

    override fun init() {
        bind(SplashViewModel::class.java)

    }

    override fun onViewModelCreated(viewModel: SplashViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySplashBinding) {
        viewDataBinding.progressBar.animate()
        CoroutineScope(Main).launch {
            delay(2000)
            viewModel.configureAppNavigation()
        }
    }

    override fun navigateToOnBoarding() {
        startActivity(
            intentProviderFactory.create(
                OnBoardingActivity::class.java,
                null,
                0
            )
        )
        finish()
    }

    override fun navigateToSignIn() {
        startActivity(
            intentProviderFactory.create(
                SignUpOptionsActivity::class.java,
                null,
                0
            )
        )
        finish()
    }

    override fun navigateToDashboard() {
        startActivity(
            intentProviderFactory.create(
                DashboardActivity::class.java,
                null,
                0
            )
        )
        finish()
    }

}
