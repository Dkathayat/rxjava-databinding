package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.databinding.ItemMyfollowersOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemMyFollowerOptionViewModel

class OptionsAdapterMyFollowers(
    val options: MutableList<String>,
    val myFollowers: MyFollowers,
    val listener: (String) -> Unit,
) : BaseRecyclerAdapter<OptionsAdapterMyFollowers.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyfollowersOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_myfollowers_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemMyFollowerOptionViewModel(
                options[position],
                position,
                myFollowers,
                listener
            )
        )
    }

    class ViewHolder(val binding: ItemMyfollowersOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemMyFollowerOptionViewModel) {
            binding.viewModel = viewModel
        }
    }


}