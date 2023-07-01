package com.yewapp.ui.modules.brandmodel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.sports.Brand
import com.yewapp.databinding.ItemBrandBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.brandmodel.vm.ItemBrandLisViewModel

class BrandLisAdapter(val brandList: MutableList<Brand>) :
    BaseRecyclerAdapter<BrandLisAdapter.ViewHolder, Brand>(brandList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBrandBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemBrandLisViewModel(brandList[position]))
    }

    fun addItems(list: List<Brand>) {
        brandList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemBrandBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemBrandLisViewModel) {
            binding.viewModel = viewModel
        }
    }
}