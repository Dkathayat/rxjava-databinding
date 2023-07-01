package com.yewapp.ui.modules.feed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.databinding.FeedsItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.feed.vm.CreateFeedImageItemViewModel


class FeedsImageAdapter(
    val list: MutableList<String>,
    val listener: CreateFeedImageItemViewModel.OnItemClickListener
) : BaseRecyclerAdapter<FeedsImageAdapter.FeedsViewHolder, String>(list) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsViewHolder =
        FeedsViewHolder(
            FeedsItemBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )


    override fun onBindViewHolder(holder: FeedsViewHolder, position: Int) {
        holder.bind(CreateFeedImageItemViewModel(list[position], listener, position))
    }

    class FeedsViewHolder(var binding: FeedsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("WrongConstant")
        fun bind(viewModel: CreateFeedImageItemViewModel) {
            binding.viewModel = viewModel

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}