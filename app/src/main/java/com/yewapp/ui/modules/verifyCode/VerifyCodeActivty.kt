package com.yewapp.ui.modules.verifyCode

import android.content.Intent
import android.os.CountDownTimer
import com.yewapp.R
import com.yewapp.databinding.ActivityVerifyCodeBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.signup.SignUpSuccessActivity
import com.yewapp.ui.modules.signup.extras.SignUpSuccessExtras
import com.yewapp.utils.RESEND_CODE_TIME
import com.yewapp.utils.drawUnderline

class VerifyCodeActivity :
    BaseActivity<VerifyCodeNavigator, VerifyCodeViewModel, ActivityVerifyCodeBinding>(),
    VerifyCodeNavigator {
    override fun getLayout(): Int = R.layout.activity_verify_code

    override fun init() {
        bind(VerifyCodeViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: VerifyCodeViewModel) {
        viewModel.setNavigator(this)
        setTimer()
    }

    override fun onViewBound(viewDataBinding: ActivityVerifyCodeBinding) {

        //Formats the email Id of the User shown in the Verification Code instruction
        setSpannableTextWithColor(
            viewDataBinding.tvOtpInstruction,
            R.string.verification_code_sent_on_email,
            viewModel.email ?: "",
            R.color.black
        )

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
        startActivity(
            intentProviderFactory.create(
                SignUpSuccessActivity::class.java,
                SignUpSuccessExtras.signUpSuccessExtras {
                    email = viewModel.email ?: return
                },
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK // added by krishna
            )
        )

    }

    override fun onResendClick() {
        setTimer()
    }


}