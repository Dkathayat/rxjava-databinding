package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.databinding.ItemAssociateOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemAssociateOptionViewModel

class OptionAssociateAdapter(
    val options: MutableList<String>,
    val associateItem: Associate,
    val listener: (String) -> Unit,
) : BaseRecyclerAdapter<OptionAssociateAdapter.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAssociateOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_associate_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemAssociateOptionViewModel(
                options[position],
                position,
                associateItem,
                listener
            )
        )
    }

    class ViewHolder(val binding: ItemAssociateOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemAssociateOptionViewModel) {
            binding.viewModel = viewModel
        }
    }
}