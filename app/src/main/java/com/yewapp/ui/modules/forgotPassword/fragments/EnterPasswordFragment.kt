package com.yewapp.ui.modules.forgotPassword.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yewapp.R
import com.yewapp.databinding.LayoutEnterPasswordFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.forgotPassword.navigators.ResetPasswordNavigator
import com.yewapp.ui.modules.forgotPassword.vm.ResetPasswordViewModel

class EnterPasswordFragment(override val layoutId: Int = R.layout.layout_enter_password_fragment) :
    BaseFragment<ResetPasswordNavigator, ResetPasswordViewModel, LayoutEnterPasswordFragmentBinding>(),
    ResetPasswordNavigator {

    var changePageListener: () -> Unit = {}

    constructor(changePageListener: () -> Unit) : this() {
        this.changePageListener = changePageListener
    }

    override fun onViewModelCreated(viewModel: ResetPasswordViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: LayoutEnterPasswordFragmentBinding) {

    }

    override fun onSuccess() {
        changePageListener()
    }

    override fun onResendClick() {

    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(ResetPasswordViewModel::class.java, inflater, container)
    }


}