package com.yewapp.ui.modules.migrateassociate

import com.yewapp.R
import com.yewapp.databinding.ActivityMigrateAssociateBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.migrateassociatesucess.MigrateAssociateSuccessActivity

class MigrateAssociateActivity :
    BaseActivity<MigrateAssociateNavigator, MigrateAssociateViewModel, ActivityMigrateAssociateBinding>(),
    MigrateAssociateNavigator {
    override fun getLayout(): Int = R.layout.activity_migrate_associate

    override fun init() {
        bind(MigrateAssociateViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: MigrateAssociateViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityMigrateAssociateBinding) {
    }

    override fun navigateToSuccessScreen() {
        startActivity(
            intentProviderFactory.create(
                MigrateAssociateSuccessActivity::class.java,
                null,
                0
            )
        )
    }


}