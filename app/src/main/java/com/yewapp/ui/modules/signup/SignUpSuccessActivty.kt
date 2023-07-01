package com.yewapp.ui.modules.signup

import android.content.Intent
import com.yewapp.R
import com.yewapp.databinding.ActivitySignUpSuccessBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.editProfile.EditProfileActivity
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras

class SignUpSuccessActivity :
    BaseActivity<SignUpSuccessNavigator, SignUpSuccessViewModel, ActivitySignUpSuccessBinding>(),
    SignUpSuccessNavigator {
    override fun getLayout(): Int = R.layout.activity_sign_up_success

    override fun init() {
        bind(SignUpSuccessViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SignUpSuccessViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySignUpSuccessBinding) {
        formatDescription()

    }

    /**
     * Formats the email Id of the User shown in the Verification Code instruction
     */
    fun formatDescription() {
        setSpannableTextWithColor(
            viewDataBinding.tvDescription,
            R.string.sign_success_info,
            viewModel.email,
            R.color.black
        )
    }

    override fun navigateToProfile() {
        startActivity(
            intentProviderFactory.create(
                EditProfileActivity::class.java,
                EditProfileExtras.editProfileExtras {
                    isSignUp = true
                },
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                //or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        finish()
    }
}