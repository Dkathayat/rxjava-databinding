package com.yewapp.ui.modules.videofeeds

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.databinding.ActivityVedioFeedsProfileBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.ConfirmationCommonDialog
import com.yewapp.ui.modules.report.ReportExtras
import com.yewapp.ui.modules.report.SendReportActivity
import com.yewapp.ui.modules.videofeeds.adapter.VideoFeedsProfileAdapter
import com.yewapp.ui.modules.videofeeds.extras.UserIdExtras
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedProfileNavigator
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedsProfileViewModel
import com.yewapp.ui.modules.videofeeds.vm.VideoFeedProfileViewModel

class VideoFeedProfileActivity :
    BaseActivity<VideoFeedProfileNavigator, VideoFeedProfileViewModel, ActivityVedioFeedsProfileBinding>(),
    VideoFeedProfileNavigator {
    override fun getLayout(): Int {
        return R.layout.activity_vedio_feeds_profile
    }

    override fun init() {
        bind(VideoFeedProfileViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: VideoFeedProfileViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityVedioFeedsProfileBinding) {
        var list = mutableListOf<VideoData>()

        var adapter =
            VideoFeedsProfileAdapter(
                list,
                object : ItemVideoFeedsProfileViewModel.OnItemClickListener {
                    override fun onItemClick(item: VideoData) {

                    }
                })

        viewDataBinding.recyclerVideoFeedsProfile.layoutManager = GridLayoutManager(this, 3)
        viewDataBinding.recyclerVideoFeedsProfile.adapter = adapter
        addObserver(adapter)

    }

    private fun addObserver(adapter: VideoFeedsProfileAdapter) {
        val viewModel = viewModel ?: return
        viewModel.videoFeedLiveList.observe(this, Observer {
            adapter.setItems(it)
        })
    }

    override fun onOptionMenuClick(option: String, id: Int) {
        viewModel.onFeedOptionSelected(option, id)

    }

    override fun onOptionSelectedClick(optionitem: String, userId: Int) {

        if (optionitem == "Report User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_feed))
                .setTitle(optionitem)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = userId
                                option = optionitem
                            },
                            0
                        )
                    )
                }.build().show(this)
        }
        if (optionitem == "Block User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_block_this_feed))
                .setTitle(optionitem)
                .addPositiveListener {
                    viewModel.toggleBlockUser(userId)
                }.build().show(this)
        }

    }

    override fun onFollowerClick() {
        val extras = UserIdExtras.userIdExtras {
            userId = viewModel.userId
            userName = viewModel.userName.get().toString()
        }
        startActivity(
            intentProviderFactory.create(
                VideoFeedUserFollowerActivity::class.java,
                extras,
                0
            )
        )

    }

    override fun onFollowingClick() {
        val extras = UserIdExtras.userIdExtras {
            userId = viewModel.userId
            userName = viewModel.userName.get().toString()
        }
        startActivity(
            intentProviderFactory.create(
                VideoFeedUserFollowerActivity::class.java,
                extras,
                0
            )
        )

    }

}