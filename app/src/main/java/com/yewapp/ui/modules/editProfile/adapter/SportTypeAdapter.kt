package com.yewapp.ui.modules.editProfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.databinding.ItemSportTypeBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.editProfile.vm.ItemSportTypeViewModel

class SportTypeAdapter(
    val sportList: MutableList<SportType>,
    val listener: ItemSportTypeViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<SportTypeAdapter.ViewHolder, SportType>(sportList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSportTypeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemSportTypeViewModel(sportList[position], position, listener))
    }

    fun addItems(list: List<SportType>) {
        sportList.clear()
        sportList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemSportTypeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemSportTypeViewModel) {
            binding.viewModel = viewModel
        }
    }
}