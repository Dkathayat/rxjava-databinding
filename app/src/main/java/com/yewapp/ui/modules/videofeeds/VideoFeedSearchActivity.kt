package com.yewapp.ui.modules.videofeeds

import com.yewapp.R
import com.yewapp.databinding.ActivityVedioFeedsSearchBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedSearchNavigator
import com.yewapp.ui.modules.videofeeds.vm.VideoFeedSearchViewModel

class VideoFeedSearchActivity :
    BaseActivity<VideoFeedSearchNavigator, VideoFeedSearchViewModel, ActivityVedioFeedsSearchBinding>(),
    VideoFeedSearchNavigator {
    override fun getLayout(): Int {
        return R.layout.activity_vedio_feeds_search
    }

    override fun init() {
        bind(VideoFeedSearchViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: VideoFeedSearchViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityVedioFeedsSearchBinding) {
//        var list = mutableListOf<String>()
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//
//        var adapter =
//            VideoFeedsProfileAdapter(
//                list,
//                object : ItemVideoFeedsProfileViewModel.OnItemClickListener {
//                    override fun onItemClick(item: String) {
//
//                    }
//                })
//
//        viewDataBinding.recyclerVideoFeedsProfile!!.layoutManager =
//            GridLayoutManager(this, 3)
//        viewDataBinding.recyclerVideoFeedsProfile!!.adapter = adapter
    }
}