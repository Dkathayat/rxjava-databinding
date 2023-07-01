package com.yewapp.ui.common.removeaccounttemp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yewapp.R
import com.yewapp.databinding.RemoveAccountBottomSheetBinding
import com.yewapp.ui.base.BaseBottomSheet

class RemoveAccountBottomSheet(override val layoutId: Int = R.layout.remove_account_bottom_sheet) :
    BaseBottomSheet<RemoveAccountNavigator, RemoveAccountViewModel, RemoveAccountBottomSheetBinding>(),
    RemoveAccountNavigator {


    override fun onViewModelCreated(viewModel: RemoveAccountViewModel) {
        viewModel.setNavigator(this)
    }

    companion object {
        lateinit var listener: OnRemoveClicked

        fun newInstance(
            message: String,
            listener: RemoveAccountBottomSheet.OnRemoveClicked
        ): RemoveAccountBottomSheet {//can not send without bundle else will give null pointer error
            this.listener = listener
            val fragment = RemoveAccountBottomSheet()
            val args = Bundle()

            val extras = RemoveAccountBottomSheetExtras.removeAccountExtras {
                this.message = message

            }
            args.putAll(extras)
            fragment.arguments = args
            return fragment
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return bind(RemoveAccountViewModel::class.java, inflater, container)
    }


    override fun onViewBound(viewDataBinding: RemoveAccountBottomSheetBinding) {
    }

    override fun onDismissClicked() {
        listener.onDismissClicked()
    }

    override fun onRemoveClicked() {
        listener.onRemoveClicked()
    }


    override fun onBackPress() {
    }

    override fun navigateToSetting() {
    }

    override fun showProfileCompletionAlert(message: String) {
    }



    interface OnRemoveClicked {
        fun onDismissClicked()
        fun onRemoveClicked()
    }

}