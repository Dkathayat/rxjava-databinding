package com.yewapp.ui.modules.addassociatemember

import com.yewapp.R
import com.yewapp.databinding.ActivityAddMembersBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.DatePicker
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsActivity
import com.yewapp.ui.modules.addassociatememberdetails.fragment.extras.AddMemberDetailsExtras
import com.yewapp.ui.modules.profile.navigator.AddMembersNavigator
import com.yewapp.utils.parseToUTC

class AddMembersActivity :
    BaseActivity<AddMembersNavigator, AddMembersViewModel, ActivityAddMembersBinding>(),
    AddMembersNavigator {

    override fun getLayout(): Int = R.layout.activity_add_members

    override fun init() {
        bind(AddMembersViewModel::class.java)

    }

    override fun onViewModelCreated(viewModel: AddMembersViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAddMembersBinding) {

    }

    override fun onDobClicked() {
        hideKeyboard()
        DatePicker.Builder()
            .isRangeCalendar(false)
            .setmConfirmListener {
                if (it.isNullOrEmpty()) return@setmConfirmListener
                viewModel.dob.set(it.parseToUTC("dd/MM/yyyy"))
                viewModel.dobSent.set(it.parseToUTC())
            }
            .build()
            .show(supportFragmentManager)
    }

    override fun navigateToAddMemberDetails() {
        val extras = AddMemberDetailsExtras.memberExtras {
            email = viewModel.email
            dob = viewModel.dob.get() ?: return
            password = viewModel.password.get() ?: return
        }
        startActivity(
            intentProviderFactory.create(
                AddMembersDetailsActivity::class.java,
                extras,
                0
            )
        )
    }


}