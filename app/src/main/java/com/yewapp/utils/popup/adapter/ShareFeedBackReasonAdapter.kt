package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.report.ReportReason
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.databinding.ItemReasonBinding
import com.yewapp.databinding.ItemReasonFeedbackBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemReasonFeebackViewModel
import com.yewapp.utils.popup.vm.ItemReasonViewModel

class ShareFeedBackReasonAdapter(val list: MutableList<Sport>, val listener: (Sport) -> Unit) :
    BaseRecyclerAdapter<ShareFeedBackReasonAdapter.ViewHolder, Sport>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReasonFeedbackBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_reason_feedback, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemReasonFeebackViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemReasonFeedbackBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemReasonFeebackViewModel) {
            binding.viewModel = viewModel
        }
    }
}