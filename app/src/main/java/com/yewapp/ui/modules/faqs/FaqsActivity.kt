package com.yewapp.ui.modules.faqs

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.faqs.FaqData
import com.yewapp.databinding.ActivityFaqsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.faqs.adapter.FaqsAdapter
import com.yewapp.ui.modules.faqs.navigator.FaqsNavigator
import com.yewapp.ui.modules.faqs.vm.FaqsViewModel

class FaqsActivity : BaseActivity<FaqsNavigator, FaqsViewModel, ActivityFaqsBinding>(),
    FaqsNavigator {

    lateinit var adapter: FaqsAdapter
    override fun getLayout(): Int = R.layout.activity_faqs

    override fun init() {
        bind(FaqsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: FaqsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityFaqsBinding) {
        var list = mutableListOf<FaqData>()

        adapter = FaqsAdapter(list)

        adapter.setHasStableIds(true)
        viewDataBinding.faqsRecyclerView.adapter = adapter
        viewDataBinding.faqsRecyclerView.layoutManager = LinearLayoutManager(this)
        addObserver()
    }

    // observer
    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.listLive.observe(this, Observer {
            this.adapter.setItems(it)
        })
    }

    // adapter
}