package com.yewapp.ui.modules.emailphonechangeverifyotp

import android.content.Intent
import android.os.CountDownTimer
import com.yewapp.R
import com.yewapp.databinding.ActivityEmailPhoneChnageVerifyCodeBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.SuccessDialog
import com.yewapp.ui.modules.login.LoginActivity
import com.yewapp.ui.modules.addchallenge.challengeroutes.EmailPhoneChangeEnum
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.utils.RESEND_CODE_TIME
import com.yewapp.utils.drawUnderline

class EmailPhoneChangeVerifyCodeActivity :
    BaseActivity<EmailPhoneChangeVerifyCodeNavigator, EmailPhoneChangeVerifyCodeViewModel, ActivityEmailPhoneChnageVerifyCodeBinding>(),
    EmailPhoneChangeVerifyCodeNavigator {
    override fun getLayout(): Int = R.layout.activity_email_phone_chnage_verify_code

    override fun init() {
        bind(EmailPhoneChangeVerifyCodeViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: EmailPhoneChangeVerifyCodeViewModel) {
        viewModel.setNavigator(this)
        setTimer()
    }

    override fun onViewBound(viewDataBinding: ActivityEmailPhoneChnageVerifyCodeBinding) {

    }

    //Sets the Countdown timer until the User can resend the Code
    private fun setTimer() {
        object : CountDownTimer(RESEND_CODE_TIME, 1000) {
            override fun onTick(p0: Long) {
                viewDataBinding.tvTimer.isEnabled = false
                viewDataBinding.tvTimer.text = String.format(
                    getString(R.string.timer_format),
                    0,
                    p0 / 1000
                ).drawUnderline()
            }

            override fun onFinish() {
                viewDataBinding.tvTimer.isEnabled = true
                viewDataBinding.tvTimer.text = getString(R.string.resend_code).drawUnderline()
            }

        }.start()
    }

    override fun onVerificationSuccess() {
        if (viewModel.type == EmailPhoneChangeEnum.EMAIL.Type) {
            SuccessDialog.Builder()
                .setDescription(
                    viewModel.dataManager.getResourceProvider()
                        .getString(R.string.email_updated_success_message)
                )
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            LoginActivity::class.java,
                            null,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK // added by krishna
                        )
                    )
                }.build().show(this)
            // onSuccess(viewModel.dataManager.getResourceProvider().getString(R.string.email_updated_success_message))

        } else if (viewModel.type == EmailPhoneChangeEnum.MOBILE.Type) {
            SuccessDialog.Builder()
                .setDescription(
                    viewModel.dataManager.getResourceProvider()
                        .getString(R.string.mobile_updated_success_message)
                )
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            DashboardActivity::class.java,
                            null,
                            0
                        )
                    )
                }.build().show(this)

        }

    }

    override fun onResendClick() {
        setTimer()
    }

    override fun resendEmailSuccess(message: String) {
        onSuccess(message)
    }

    override fun onLogoutSuccess() {
        onSessionExpired()
    }

    override fun setMessageForEmail(isEmailVerify: Boolean) {
        if (isEmailVerify) {
            //Formats the email Id of the User shown in the Verification Code instruction
            setSpannableTextWithColor(
                viewDataBinding.tvOtpInstruction,
                R.string.verification_code_sent_on_email,
                viewModel.email ?: "",
                R.color.black
            )
        } else {
            //Formats the Mobile Number of the User shown in the Verification Code instruction
            setSpannableTextWithColor(
                viewDataBinding.tvOtpInstruction,
                R.string.verification_code_sent_on_mobile,
                viewModel.mobile ?: "",
                R.color.black
            )
        }
    }
}