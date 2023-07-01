package com.yewapp.ui.dialogs.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.databinding.ItemProfileSingleSelectionPopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class ProfileSingleSelectionAdapter(  val list: MutableList<String>,
                                      val listener: (String) -> Unit
) :
    BaseRecyclerAdapter<ProfileSingleSelectionAdapter.ViewHolder, String>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProfileSingleSelectionPopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_profile_single_selection_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemProfileSingleSelectionViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemProfileSingleSelectionPopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemProfileSingleSelectionViewModel) {
            binding.viewModel = viewModel
        }
    }
}