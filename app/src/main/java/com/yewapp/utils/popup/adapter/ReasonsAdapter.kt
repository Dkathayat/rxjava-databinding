package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.report.ReportReason
import com.yewapp.databinding.ItemReasonBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemReasonViewModel

class ReasonsAdapter(val list: MutableList<ReportReason>, val listener: (ReportReason) -> Unit) :
    BaseRecyclerAdapter<ReasonsAdapter.ViewHolder, ReportReason>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReasonBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_reason, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemReasonViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemReasonBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemReasonViewModel) {
            binding.viewModel = viewModel
        }
    }
}