package com.yewapp.ui.modules.migrateassociatesucess

import com.yewapp.R
import com.yewapp.databinding.ActivityAssociateMigrateSuccessBinding
import com.yewapp.ui.base.BaseActivity

class MigrateAssociateSuccessActivity :
    BaseActivity<MigrateAssociateSuccessNavigator, MigrateAssociateSuccessViewModel, ActivityAssociateMigrateSuccessBinding>(),
    MigrateAssociateSuccessNavigator {
    override fun getLayout(): Int = R.layout.activity_associate_migrate_success

    override fun init() {
        bind(MigrateAssociateSuccessViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: MigrateAssociateSuccessViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAssociateMigrateSuccessBinding) {
    }
}