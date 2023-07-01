package com.yewapp.ui.modules.onboarding

import androidx.viewpager.widget.ViewPager
import com.yewapp.R
import com.yewapp.databinding.ActivityOnboardingBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.signup.SignUpOptionsActivity
import javax.inject.Inject

class OnBoardingActivity :
    BaseActivity<OnBoardingNavigator, OnBoardingViewModel, ActivityOnboardingBinding>(),
    OnBoardingNavigator {

    @Inject
    lateinit var adapter: OnBoardingAdapter

    override fun getLayout(): Int = R.layout.activity_onboarding

    override fun init() {
        bind(OnBoardingViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: OnBoardingViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityOnboardingBinding) {
        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.viewPager.addOnPageChangeListener(viewPagerListener)

    }

    override fun scrollPagerToPosition(position: Int) {
        viewDataBinding.viewPager.setCurrentItem(position, true)
    }

    override fun navigateToSignUpOptionScreen() {
        startActivity(
            intentProviderFactory.create(
                SignUpOptionsActivity::class.java,
                null,
                0
            )
        )
    }

    private val viewPagerListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            viewModel.changeUi(position)
        }
    }
}