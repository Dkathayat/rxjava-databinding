package com.yewapp.ui.modules.forgotPassword.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yewapp.R
import com.yewapp.databinding.LayoutVerifyCodeFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.forgotPassword.navigators.ResetPasswordNavigator
import com.yewapp.ui.modules.forgotPassword.vm.ResetPasswordViewModel
import com.yewapp.utils.RESEND_CODE_TIME
import com.yewapp.utils.drawUnderline

class VerifyCodeFragment(override val layoutId: Int = R.layout.layout_verify_code_fragment) :
    BaseFragment<ResetPasswordNavigator, ResetPasswordViewModel, LayoutVerifyCodeFragmentBinding>(),
    ResetPasswordNavigator {
    var changePageListener: () -> Unit = {}

    lateinit var timer: CountDownTimer

    constructor(changePageListener: () -> Unit) : this() {
        this.changePageListener = changePageListener
    }

    override fun onViewModelCreated(viewModel: ResetPasswordViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: LayoutVerifyCodeFragmentBinding) {
        formatInstruction()
        setTimer()
        viewDataBinding.tvTimer.setOnClickListener {
            if (viewModel?.timerFinish ?: return@setOnClickListener) {
                viewModel?.onResendCodeClick(it)
                setTimer()

            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    fun formatInstruction() {
        //Formats the email Id of the User shown in the Verification Code instruction
        setSpannableTextWithColor(
            viewDataBinding.tvOtpInstruction,
            R.string.verification_code_sent_on_email,
            viewModel?.email ?: "",
            R.color.black
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(ResetPasswordViewModel::class.java, inflater, container)
    }


    //Sets the Countdown timer until the User can resend the Code
    private fun setTimer() {
        viewModel?.timerFinish = false
        timer = object : CountDownTimer(RESEND_CODE_TIME, 1000) {
            override fun onTick(p0: Long) {
                viewDataBinding.tvTimer.text = String.format(
                    activity?.getString(R.string.timer_format) ?: return,
                    0,
                    p0 / 1000
                ).drawUnderline()
            }

            override fun onFinish() {
                viewModel?.timerFinish = true
                viewDataBinding.tvTimer.text = getString(R.string.resend_code).drawUnderline()
            }

        }.start()
    }

    override fun onSuccess() {
        changePageListener()
    }

    override fun onResendClick() {
        setTimer()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onBackPress() {

    }
}