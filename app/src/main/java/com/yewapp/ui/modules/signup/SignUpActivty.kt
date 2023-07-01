package com.yewapp.ui.modules.signup

import android.content.Context
import com.yewapp.R
import com.yewapp.data.local.PreferenceKeys
import com.yewapp.databinding.ActivitySignUpBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.DatePicker
import com.yewapp.ui.modules.about.CmsExtras
import com.yewapp.ui.modules.cms.CmsActivity
import com.yewapp.ui.modules.signup.navigator.SignUpNavigator
import com.yewapp.ui.modules.signup.vm.SignUpViewModel
import com.yewapp.ui.modules.verifyCode.VerifyCodeActivity
import com.yewapp.ui.modules.verifyCode.extras.VERIFY
import com.yewapp.ui.modules.verifyCode.extras.VerifyCodeExtras
import com.yewapp.utils.PREF_NAME
import com.yewapp.utils.getDateTime
import com.yewapp.utils.is18
import com.yewapp.utils.parseToUTC

class SignUpActivity : BaseActivity<SignUpNavigator, SignUpViewModel, ActivitySignUpBinding>(),
    SignUpNavigator {
    override fun getLayout(): Int = R.layout.activity_sign_up


    override fun init() {
        bind(SignUpViewModel::class.java)

    }

    override fun onViewModelCreated(viewModel: SignUpViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySignUpBinding) {
        viewModel.fcmToken = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
            PreferenceKeys.FIREBASE_TOKEN,"empty").toString()

    }

    override fun onSignUpSuccess() {
        startActivity(
            intentProviderFactory.create(
                VerifyCodeActivity::class.java,
                VerifyCodeExtras.verifyCodeExtras {
                    email = viewModel.email
                    from = VERIFY.FROM_SIGN_UP.type
                },
                0
            )
        )
    }

    override fun onDobClicked() {
        DatePicker.Builder()
            .isRangeCalendar(false)
            .setmConfirmListener {
                if (it.isNullOrEmpty()) return@setmConfirmListener
                val dobSelected = it.parseToUTC("dd/MM/yyyy")
                //val now = Date()
                if (!is18(
                        getDateTime(dobSelected),
                        getDateTime("")
                    )
                ) {
                    onError(Throwable(getString(R.string.only_18_users_are_allowed_to_create_a_yew_account)))
                    return@setmConfirmListener
                }

                viewModel.dob.set(dobSelected)
                viewModel.dobSent = it.parseToUTC()
            }
            .build()
            .show(supportFragmentManager)
    }

    override fun navigateToTermsAndCondition() {
        startActivity(
            intentProviderFactory.create(
                CmsActivity::class.java,
                CmsExtras.cmsExtras {
                    link = "terms-of-use"
                },
                0
            )
        )
    }

    override fun navigateToPrivacyPolicy() {
        startActivity(
            intentProviderFactory.create(
                CmsActivity::class.java,
                CmsExtras.cmsExtras {
                    link = "privacy-policy"
                },
                0
            )
        )
    }
}