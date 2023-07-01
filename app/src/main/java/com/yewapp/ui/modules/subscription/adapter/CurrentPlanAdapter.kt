package com.yewapp.ui.modules.subscription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.subscription.History
import com.yewapp.data.network.api.subscription.purchaseHistory
import com.yewapp.databinding.SubscriptionHistoryItemsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.DateUtils

class CurrentPlanAdapter(
    context: Context
) : RecyclerView.Adapter<CurrentPlanAdapter.ViewHolder>() {

    class ViewHolder(val binding: SubscriptionHistoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(history: purchaseHistory){
                binding.planName.text = history.name
                binding.purchaseDate.text = DateUtils.getDateFromTimeStamp(history.purchase_date.toLong())
                binding.purchaseAmount.text = history.plan
            }
    }

    private val differCallback = object : DiffUtil.ItemCallback<purchaseHistory>() {
        override fun areItemsTheSame(oldItem: purchaseHistory, newItem: purchaseHistory): Boolean {
            return oldItem.purchase_date == newItem.purchase_date
        }

        override fun areContentsTheSame(oldItem: purchaseHistory, newItem: purchaseHistory): Boolean {
            return oldItem == newItem
        }
    }

     val differ = AsyncListDiffer(this@CurrentPlanAdapter,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SubscriptionHistoryItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}