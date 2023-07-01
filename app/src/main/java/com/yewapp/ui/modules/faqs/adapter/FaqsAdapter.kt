package com.yewapp.ui.modules.faqs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.faqs.FaqData
import com.yewapp.databinding.ItemFaqsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.faqs.vm.ItemsFaqsViewModel

class FaqsAdapter(
    val faqList: MutableList<FaqData>

) :
    BaseRecyclerAdapter<FaqsAdapter.ViewHolder, FaqData>(faqList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemFaqsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemsFaqsViewModel(faqList[position]))
    }

    class ViewHolder(val binding: ItemFaqsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemsFaqsViewModel) {

            binding.viewModel = viewModel
        }
    }

}