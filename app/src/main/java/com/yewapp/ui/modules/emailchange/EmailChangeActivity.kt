package com.yewapp.ui.modules.emailchange

import com.yewapp.R
import com.yewapp.data.network.api.profile.Country
import com.yewapp.databinding.EmailPhoneChangeActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.modules.emailchange.navigator.EmailChangeNavigator
import com.yewapp.ui.modules.emailchange.vm.EmailChangeViewModel
import com.yewapp.ui.modules.emailphonechangeverifyotp.EmailPhoneChangeVerifyCodeActivity
import com.yewapp.ui.modules.emailphonechangeverifyotp.extras.EmailPhoneChangeVerifyCodeExtras
import com.yewapp.ui.modules.addchallenge.challengeroutes.EmailPhoneChangeEnum

class EmailChangeActivity :
    BaseActivity<EmailChangeNavigator, EmailChangeViewModel, EmailPhoneChangeActivityBinding>(),
    EmailChangeNavigator {
    override fun getLayout(): Int {
        return R.layout.email_phone_change_activity
    }

    override fun init() {
        bind(EmailChangeViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: EmailChangeViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: EmailPhoneChangeActivityBinding) {
    }

    override fun selectCountryCodeClick() {
        showBottomSheet(
            getString(R.string.choose_country_code),
            viewModel.countryList as MutableList<Country>
        ) {
//            viewModel.selectedCountryId = it.id
            viewModel.countryID.set(it.countryCode)
        }
    }

    override fun changeEmailSuccess(message: String) {
        onSuccess(message)
        if (viewModel.type == EmailPhoneChangeEnum.EMAIL.Type) {
            startActivity(
                intentProviderFactory.create(
                    EmailPhoneChangeVerifyCodeActivity::class.java,
                    EmailPhoneChangeVerifyCodeExtras.emailPhoneChangeVerifyCodeExtras {
                        isEmailChange = true
                        isPhoneChange = false
                        email = viewModel.newEmail.get().toString()
                        mobile = ""
                        countrycode = ""
                    },
                    0
                )
            )
        } else {
            startActivity(
                intentProviderFactory.create(
                    EmailPhoneChangeVerifyCodeActivity::class.java,
                    EmailPhoneChangeVerifyCodeExtras.emailPhoneChangeVerifyCodeExtras {
                        isEmailChange = false
                        isPhoneChange = true
                        email = ""
                        mobile = viewModel.mobileNumber.get() ?: return
                        countrycode = viewModel.countryID.get() ?: return
                    },
                    0
                )
            )
        }
    }


    fun <T> showBottomSheet(title: String, list: MutableList<T>, listener: (T) -> Unit) {
        GenericListBottomSheet.Builder<T>()
            .setTitleText(title)
            .setDataList(list)
            .setClickListener {
                listener(it)
            }
            .build()
            .show(this)
    }

}