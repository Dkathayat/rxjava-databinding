package com.yewapp.ui.modules.viewMedia.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.feed.Files
import com.yewapp.databinding.ImageVideoItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.viewMedia.vm.ItemViewMediaViewModel


class FeedsMediaAdapter(
    val list: MutableList<Files>,
    val listener: ItemViewMediaViewModel.OnItemClickListener
) : BaseRecyclerAdapter<FeedsMediaAdapter.FeedsViewHolder, Files>(list) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsViewHolder =
        FeedsViewHolder(
            ImageVideoItemBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: FeedsViewHolder, position: Int) {
        holder.bind(ItemViewMediaViewModel(list[position], listener, position))
    }

    class FeedsViewHolder(var binding: ImageVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("WrongConstant")
        fun bind(viewModel: ItemViewMediaViewModel) {
            binding.viewModel = viewModel

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}