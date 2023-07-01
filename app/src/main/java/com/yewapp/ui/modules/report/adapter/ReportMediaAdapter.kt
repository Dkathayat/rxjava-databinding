package com.yewapp.ui.modules.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.databinding.ItemReportMediaBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.report.vm.ItemReportMediaViewModel

class ReportMediaAdapter(
    private val sportList: MutableList<String>,
    val onItemClickListener: ItemReportMediaViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<ReportMediaAdapter.ViewHolder, String>(sportList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReportMediaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemReportMediaViewModel(sportList[position], onItemClickListener, position))

    }

    class ViewHolder(val binding: ItemReportMediaBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemReportMediaViewModel) {
            binding.viewModel = viewModel

            /*   binding.root.post {
                   binding.root.layoutParams.width = binding.root.rootView.width/3
                   binding.root.layoutParams.height = binding.root.rootView.width/3
                   binding.root.requestLayout()
               }*/
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return sportList.size
    }
}