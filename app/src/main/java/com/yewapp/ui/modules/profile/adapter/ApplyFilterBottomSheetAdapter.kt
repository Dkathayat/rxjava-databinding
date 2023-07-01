package com.yewapp.ui.modules.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yewapp.R
import com.yewapp.data.network.api.profile.FilterData
import com.yewapp.databinding.ItemBottomSheetActivityFilterBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.profile.fragment.bottomsheet.ItemBottomSheetFilterViewModel

class ApplyFilterBottomSheetAdapter(
    var list: MutableList<FilterData>,
    val listener: ItemBottomSheetFilterViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<ApplyFilterBottomSheetAdapter.ViewHolderFilter, FilterData>(list) {

    class ViewHolderFilter(val binding: ItemBottomSheetActivityFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var isAllSelected = binding.referCheckbox
        fun bind(viewModel: ItemBottomSheetFilterViewModel) {
            binding.viewModel = viewModel
            binding.tvNameFilter.text = viewModel.item.name


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFilter {
        return ViewHolderFilter(
            ItemBottomSheetActivityFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderFilter, position: Int) {
        holder.bind(
            ItemBottomSheetFilterViewModel(
                list[position],
                listener

            )
        )

    }

    fun clearItem(){
        for(i in list){
            i.isSelected =false
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}