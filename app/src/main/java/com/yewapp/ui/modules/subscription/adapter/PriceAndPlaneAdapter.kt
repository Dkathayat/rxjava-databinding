package com.yewapp.ui.modules.subscription.adapter

import android.telephony.SubscriptionPlan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.subscription.PlanDetails
import com.yewapp.databinding.ItemsPlansSubscriptionBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.subscription.vm.ItemPlansPricingSubscriptionViewModel

class PriceAndPlaneAdapter(
    private val subscriptionPlan: MutableList<PlanDetails>,
) : BaseRecyclerAdapter<RecyclerView.ViewHolder, PlanDetails>(subscriptionPlan) {

    class PlansViewHolder(val binding: ItemsPlansSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel:ItemPlansPricingSubscriptionViewModel){
            binding.viewModel = viewModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlansViewHolder(
            ItemsPlansSubscriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlansViewHolder).bind(
            ItemPlansPricingSubscriptionViewModel(
                subscriptionPlan[position],
                position
            )
        )
    }
}