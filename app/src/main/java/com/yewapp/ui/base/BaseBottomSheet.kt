package com.yewapp.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.yewapp.BR
import com.yewapp.ui.modules.login.LoginActivity
import com.yewapp.utils.factory.IntentProviderFactory
import com.yewapp.utils.factory.ViewModelProviderFactory
import com.yewapp.utils.httpErrorMap
import com.yewapp.utils.showError
import com.yewapp.utils.showSuccess
import dagger.android.support.AndroidSupportInjection
import retrofit2.HttpException
import javax.inject.Inject

abstract class BaseBottomSheet<N, VM : BaseViewModel<N>, VB : ViewDataBinding> :
    BottomSheetDialogFragment(), View.OnClickListener, BaseNavigator {

    protected abstract val layoutId: Int

    lateinit var viewDataBinding: VB
    var viewModel: VM? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    @Inject
    lateinit var intentProviderFactory: IntentProviderFactory

    abstract fun onViewModelCreated(viewModel: VM)
    abstract fun onViewBound(viewDataBinding: VB)
//    var bottomSheet: BottomSheetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onDetach() {
        super.onDetach()
    }

    private fun <T : BaseViewModel<N>> getViewModel(viewModelClass: Class<T>): T {

        return ViewModelProvider(requireActivity(), viewModelFactory).get(viewModelClass)

        //return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }


    fun bind(viewModelClass: Class<VM>, inflater: LayoutInflater, container: ViewGroup?): View {
        viewModel = if (viewModel == null) getViewModel(viewModelClass) else viewModel
        onViewModelCreated(viewModel!!)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding.setVariable(BR.viewModel, viewModel)
        viewDataBinding.executePendingBindings()
        onViewBound(viewDataBinding)
        viewModel!!.setData(arguments)
        return viewDataBinding.root
    }

    override fun onError(t: Throwable, showAction: Boolean, function: () -> Unit) {
        if (t is HttpException) {
            when (val code = t.code()) {
                401 -> {
                    activity?.showError(httpErrorMap[code] ?: return, showAction = false)
                    Handler().postDelayed({

                        onSessionExpired()
                    }, 2000)
                }
                else -> activity?.showError(httpErrorMap[code] ?: return, showAction = true) {
                    function()
                }
            }

        } else {
            activity?.showError(t.message ?: return, showAction = showAction) {
                function()
            }
        }
    }

    override fun onSuccess(message: String, showAction: Boolean, function: () -> Unit) {
        activity?.showSuccess(message, showAction, function)
    }

    fun onSessionExpired() {
        //Call activity on Session Expire
        FirebaseAuth.getInstance().signOut()
        startActivity(
            intentProviderFactory.create(
                LoginActivity::class.java,
                null,
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
        viewModel?.clearFlags()
        activity?.finish()
    }

    override fun onClick(v: View?) {

    }
}