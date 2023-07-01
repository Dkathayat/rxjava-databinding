package com.yewapp.ui.modules.managefeeds.fragment.reported

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.report.ReportedFeedFile
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.databinding.ItemReportedFeedBinding
import com.yewapp.databinding.ItemReportedFeedWithoutimageBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.managefeeds.extras.ItemReportedFeedImageAdapter
import com.yewapp.ui.modules.managefeeds.vm.ReportedFeedsViewModel

class ReportedFeedAdapter(
    private val reportedPostList: MutableList<ReportedPosts>,
    private val onReportedFeedOptionClickListener: ReportedFeedsViewModel.OnReportedFeedOptionClickListener,

    ) :
    BaseRecyclerAdapter<RecyclerView.ViewHolder, ReportedPosts>(reportedPostList) {

    companion object {
        const val VIEW_TYPE_WITH_IMAGE = 1
        const val VIEW_TYPE_WITHOUT_IMAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_WITH_IMAGE) {
            return ViewHolderWithImage(
                ItemReportedFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewHolderWithoutImage(
                ItemReportedFeedWithoutimageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (reportedPostList[position].files.isNotEmpty()) {
            (holder as ViewHolderWithImage).bind(
                ReportedFeedsViewModel(
                    reportedPostList[position],
                    position,
                    onReportedFeedOptionClickListener
                )
            )
            var imageList = arrayListOf<ReportedFeedFile>()
            var videoList = arrayListOf<ReportedFeedFile>()
            if (reportedPostList[position].files.isNullOrEmpty()) {
                holder.binding.wormDotsIndicator.visibility = View.GONE
            } else {
                for (i in 0 until reportedPostList[position].files.size) {
                    imageList.add(reportedPostList[position].files[i])
                }
                if (reportedPostList[position].files.size == 1) {
                    holder.binding.wormDotsIndicator.visibility = View.GONE
                } else {
                    holder.binding.wormDotsIndicator.visibility = View.VISIBLE
                }
            }
            val adapterImage = ItemReportedFeedImageAdapter(
                imageList as MutableList<ReportedFeedFile>,
                reportedPostList[position]
            )
            adapterImage.setHasStableIds(true)

            holder.binding.viewPager2.apply {
                adapter = adapterImage
            }
            holder.binding.wormDotsIndicator.setViewPager2(holder.binding.viewPager2)

        } else {
            (holder as ViewHolderWithoutImage).bind(
                ReportedFeedsViewModel(
                    reportedPostList[position],
                    position,
                    onReportedFeedOptionClickListener
                )
            )
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (reportedPostList[position].files.isEmpty()) {
            VIEW_TYPE_WITHOUT_IMAGE
        } else {
            VIEW_TYPE_WITH_IMAGE
        }
    }

    fun addReportedFeedItems(list: List<ReportedPosts>) {
        reportedPostList.addAll(list)
        notifyDataSetChanged()
    }


    class ViewHolderWithImage(val binding: ItemReportedFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ReportedFeedsViewModel) {
            binding.viewModel = viewModel
        }

    }

    class ViewHolderWithoutImage(private val binding: ItemReportedFeedWithoutimageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ReportedFeedsViewModel) {
            binding.viewModel = viewModel
        }
    }


}