package com.yewapp.ui.modules.videofeedcomment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.video.Comment
import com.yewapp.data.network.api.video.Reply
import com.yewapp.databinding.BottomSheetCommentBinding
import com.yewapp.ui.base.BaseBottomSheet
import com.yewapp.ui.dialogs.ConfirmationCommonDialog
import com.yewapp.ui.modules.report.ReportExtras
import com.yewapp.ui.modules.report.SendReportActivity
import com.yewapp.ui.modules.videofeedcomment.extras.VideoFeedIdExtra
import com.yewapp.ui.modules.videofeedcomment.navigator.VideoFeedCommentNavigator
import com.yewapp.ui.modules.videofeedcomment.vm.ItemReplyViewModel
import com.yewapp.ui.modules.videofeedcomment.vm.VideoFeedCommentViewModel
import com.yewapp.ui.modules.videofeeds.adapter.VideoFeedsCommentAdapter
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedsCommentViewModel

class VideoFeedCommentBottomSheet(override val layoutId: Int = R.layout.bottom_sheet_comment) :
    BaseBottomSheet<VideoFeedCommentNavigator, VideoFeedCommentViewModel, BottomSheetCommentBinding>(),
    VideoFeedCommentNavigator {
    //val feedId:Int
    private lateinit var adapter: VideoFeedsCommentAdapter


    companion object {
        lateinit var listener: OnDismissClick

        fun newInstance(
            videoFeedId: Int,
            position: Int,
            commentCount: Int,
            listener: OnDismissClick
        ): VideoFeedCommentBottomSheet {//can not send without bundle else will give null pointer error
            this.listener = listener
            val fragment = VideoFeedCommentBottomSheet()
            val args = Bundle()

            val extras = VideoFeedIdExtra.videoFeedIdExtra {
                feedId = videoFeedId
                this.position = position
                this.commentCount = commentCount
            }
            args.putAll(extras)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//           viewModel?.feedId=it.getInt(VideoFeedIdExtra.FEED_ID)
//            println(viewModel?.feedId)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return bind(VideoFeedCommentViewModel::class.java, inflater, container)
    }


    override fun onViewModelCreated(viewModel: VideoFeedCommentViewModel) {
        viewModel.setNavigator(this)
    }

    private fun addObserver(adapter: VideoFeedsCommentAdapter) {
        val viewModel = viewModel ?: return
        viewModel.commentFeedLiveList.observe(this, Observer {
            adapter.setItems(it)
        })
    }

    override fun onViewBound(viewDataBinding: BottomSheetCommentBinding) {
        val list = arrayListOf<Comment>()


        adapter = VideoFeedsCommentAdapter(list, viewModel?.dataManager?.getUser()?.userId!!,
            object : ItemVideoFeedsCommentViewModel.OnItemClickListener {
                override fun replyComment(
                    commentItem: Comment,
                    replyText: String,
                    commentIndex: Int
                ) {
                    viewModel?.parentId = commentItem.id
                    viewModel?.onReply(replyText, commentIndex)
                }

                override fun likeComment(commentItem: Comment, position: Int) {
                    // viewModel?.selectedItem = position
                    viewModel?.onLikeComment(commentItem.id, position, position)
                }

                override fun onOptionMenuClick(
                    option: String,
                    commentItem: Comment,
                    position: Int
                ) {
                    viewModel?.onFeedOptionSelected(option, commentItem, position)
                }
            },

            object : ItemReplyViewModel.OnReplyItemClickListener {
                override fun report(commentId: Int, item: Reply, index: Int) {
                }

                override fun likeReply(commentId: Int, item: Reply, index: Int, commentIndex: Int) {
                    viewModel?.onLikeComment(item.id, index, commentIndex)
                }

                override fun onOptionMenuClick(option: String, item: Reply, position: Int) {
                    viewModel?.onReplyOptionSelected(option, item, position)
                }
            }
        )
        viewDataBinding.recyclerComment.layoutManager = LinearLayoutManager(requireActivity())
        viewDataBinding.recyclerComment.adapter = adapter
        addObserver(adapter)
    }

    override fun onSuccessResult(message: String) {
        onSuccess(message)
        viewModel?.clearList()
        adapter.clearItems()
        viewModel?.getCommentList()
    }

    override fun onOptionSelectedClick(comment: Comment, optionitem: String, position: Int) {

        if (optionitem == "Report Comment") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_comment))
                .setTitle(optionitem)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = comment.id
                                option = optionitem
                            },
                            0
                        )
                    )
                }.build().show(requireActivity())
        }
        if (optionitem == "Report User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_user))
                .setTitle(optionitem)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = comment.createdBy.id
                                option = optionitem
                            },
                            0
                        )
                    )
                }.build().show(requireActivity())
        }
        if (optionitem == "Block User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_block_this_feed))
                .setTitle(optionitem)
                .addPositiveListener {
                    viewModel?.toggleBlockUser(comment.createdBy.id)
                }.build().show(requireActivity())
        }

    }

    override fun onReplyOptionSelectedClick(reply: Reply, option: String, position: Int) {

        if (option == "Report Comment") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_comment))
                .setTitle(option)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = reply.id
                                this.option = option

                            },
                            0
                        )
                    )
                }.build().show(requireActivity())
        }
        if (option == "Report User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_feed))
                .setTitle(option)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = reply.createdBy.id
                                this.option = option
                            },
                            0
                        )
                    )
                }.build().show(requireActivity())
        }
        if (option == "Block User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_block_this_feed))
                .setTitle(option)
                .addPositiveListener {
                    viewModel?.toggleBlockUser(reply.createdBy.id)
                }.build().show(requireActivity())
        }

    }

    override fun onLikeSuccess(comment: Comment, position: Int) {
        adapter.updateLikedItem(comment, position)
    }


    override fun onBackPress() {
    }

    override fun navigateToSetting() {
    }

    override fun showProfileCompletionAlert(message: String) {
    }

//    override fun dismiss() {
//        super.dismiss()
//
//    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener.onItemClick(
            viewModel?.feedId ?: return,
            viewModel?.position ?: return,
            viewModel?.comment ?: return
        )


    }

    interface OnDismissClick {
        fun onItemClick(videoFeedId: Int, position: Int, commentCount: Int)
    }
}