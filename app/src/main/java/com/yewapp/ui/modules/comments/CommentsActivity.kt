package com.yewapp.ui.modules.comments

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.video.Reply
import com.yewapp.databinding.ActivityCommentsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.ConfirmationCommonDialog
import com.yewapp.ui.modules.comments.navigator.CommentsNavigator
import com.yewapp.ui.modules.comments.vm.CommentsViewModel
import com.yewapp.ui.modules.report.ReportExtras
import com.yewapp.ui.modules.report.SendReportActivity
import com.yewapp.ui.modules.videofeedcomment.vm.ItemReplyViewModel
import com.yewapp.ui.modules.videofeeds.adapter.VideoFeedsCommentAdapter
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedsCommentViewModel

class CommentsActivity :
    BaseActivity<CommentsNavigator, CommentsViewModel, ActivityCommentsBinding>(),
    CommentsNavigator {
    companion object {
        const val COMMENT_SCREEN = 1001
    }

    //  private lateinit var adapter: CommentsAdapter
    private lateinit var adapter: VideoFeedsCommentAdapter

    override fun getLayout(): Int = R.layout.activity_comments

    override fun init() {
        bind(CommentsViewModel::class.java)
    }


    override fun onViewModelCreated(viewModel: CommentsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityCommentsBinding) {
//        adapter = CommentsAdapter(mutableListOf())
//        adapter.setHasStableIds(true)
//        viewDataBinding.commentsRecycler.adapter = adapter
//        viewModel.getCommentList()
//        addObserver()
        val list = arrayListOf<com.yewapp.data.network.api.video.Comment>()

        adapter = VideoFeedsCommentAdapter(list, viewModel.dataManager.getUser().userId?:return,
            object : ItemVideoFeedsCommentViewModel.OnItemClickListener {
                override fun replyComment(
                    commentItem: com.yewapp.data.network.api.video.Comment,
                    replyText: String,
                    commentIndex: Int
                ) {
                    viewModel.parentId = commentItem.id
                    viewModel.onReply(replyText, commentIndex)
                }

                override fun likeComment(
                    commentItem: com.yewapp.data.network.api.video.Comment,
                    position: Int
                ) {
                    viewModel.onLikeComment(commentItem.id, position)
                }

                override fun onOptionMenuClick(
                    option: String,
                    commentItem: com.yewapp.data.network.api.video.Comment,
                    position: Int
                ) {
                    viewModel.onFeedOptionSelected(option, commentItem, position)
                }
            },

            object : ItemReplyViewModel.OnReplyItemClickListener {
                override fun report(commentId: Int, item: Reply, index: Int) {
                }

                override fun likeReply(commentId: Int, item: Reply, index: Int, commentIndex: Int) {
                    viewModel.onLikeComment(item.id, commentIndex)
                }

                override fun onOptionMenuClick(option: String, item: Reply, position: Int) {
                    viewModel.onReplyOptionSelected(option, item, position)
                }
            }
        )
        viewDataBinding.recyclerComment.layoutManager = LinearLayoutManager(this)
        viewDataBinding.recyclerComment.adapter = adapter
        addObserver(adapter)
    }


    // observer
    private fun addObserver(adapter: VideoFeedsCommentAdapter) {
//        val viewModel = viewModel ?: return
//        viewModel.allCommentList.observe(this, Observer {
//            this.adapter.setItems(it)
//        })

        val viewModel = viewModel ?: return
        viewModel.commentFeedLiveList.observe(this, Observer {
            adapter.setItems(it)
        })
    }

    override fun clearList() {
        //adapter.clearList()
    }

    override fun onOptionSelectedClick(
        comment: com.yewapp.data.network.api.video.Comment,
        selectedOption: String,
        position: Int
    ) {
        if (selectedOption == "Report Comment") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_comment))
                .setTitle(selectedOption)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = comment.id
                                option = selectedOption
                            },
                            0
                        )
                    )
                }.build().show(this)
        }
        if (selectedOption == "Report User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_feed))
                .setTitle(selectedOption)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = comment.createdBy.id
                                option = selectedOption
                            },
                            0
                        )
                    )
                }.build().show(this)
        }
        if (selectedOption == "Block User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_block_this_feed))
                .setTitle(selectedOption)
                .addPositiveListener {
                    viewModel.toggleBlockUser(comment.createdBy.id)
                }.build().show(this)
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
                }.build().show(this)
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
                }.build().show(this)
        }
        if (option == "Block User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_block_this_feed))
                .setTitle(option)
                .addPositiveListener {
                    viewModel.toggleBlockUser(reply.createdBy.id)
                }.build().show(this)
        }

    }

    override fun onSuccessResult(message: String) {
        onSuccess(message)
        viewModel.clearList()
        adapter.clearItems()
        viewModel.getCommentList()
    }

    override fun onLikeSuccess(comment: com.yewapp.data.network.api.video.Comment, position: Int) {
        adapter.updateLikedItem(comment, position)
    }

    override fun onBackClick() {
        finish()
    }

    override fun finish() {
        var intent = Intent()
        intent.putExtra("commentCount", viewModel.comment)
        intent.putExtra("index", viewModel.position)
        //   intent.putExtra("commentCount",viewModel.comment)
        setResult(COMMENT_SCREEN, intent)
        super.finish()
    }
}