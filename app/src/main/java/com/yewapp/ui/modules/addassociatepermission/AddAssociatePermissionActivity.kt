package com.yewapp.ui.modules.addassociatepermission

import android.widget.Toast
import com.yewapp.R
import com.yewapp.databinding.ActivityAddAssociatePermissionBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.dashboard.DashboardActivity

class AddAssociatePermissionActivity :
    BaseActivity<AddAssociatePermissionNavigator, AddAssociatePermissionViewModel, ActivityAddAssociatePermissionBinding>(),
    AddAssociatePermissionNavigator {
    override fun getLayout(): Int = R.layout.activity_add_associate_permission

    override fun init() {
        bind(AddAssociatePermissionViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AddAssociatePermissionViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAddAssociatePermissionBinding) {
    }

    override fun navigateToProfile(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        finish()
        startActivity(
            intentProviderFactory.create(
                DashboardActivity::class.java,
                null,
                0
            )
        )
    }
}