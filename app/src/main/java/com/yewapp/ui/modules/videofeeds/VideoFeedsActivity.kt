package com.yewapp.ui.modules.videofeeds

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.databinding.ActivityVedioFeedsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.ConfirmationCommonDialog
import com.yewapp.ui.modules.camera.CameraActivity
import com.yewapp.ui.modules.comments.CommentsActivity
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.LIKES
import com.yewapp.ui.modules.report.ReportExtras
import com.yewapp.ui.modules.report.SendReportActivity
import com.yewapp.ui.modules.videofeedcomment.VideoFeedCommentBottomSheet
import com.yewapp.ui.modules.videofeeds.adapter.VideoFeedsAdapter
import com.yewapp.ui.modules.videofeeds.extras.VideoFeedUserExtras
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedsNavigator
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedsViewModel
import com.yewapp.ui.modules.videofeeds.vm.VideoFeedsViewModel
import com.yewapp.utils.CAMERA_PERMISSION_REQUEST
import com.yewapp.utils.URIPathHelper
import com.yewapp.utils.getFeedTime

class VideoFeedsActivity :
    BaseActivity<VideoFeedsNavigator, VideoFeedsViewModel, ActivityVedioFeedsBinding>(),
    VideoFeedsNavigator {

    private lateinit var adapter: VideoFeedsAdapter

    override fun getLayout(): Int {
        return R.layout.activity_vedio_feeds
    }

    override fun init() {
        bind(VideoFeedsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: VideoFeedsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityVedioFeedsBinding) {
        setVideoFeedsAdapter()
    }

    private fun setVideoFeedsAdapter() {
        var list = arrayListOf<VideoData>()

        adapter = VideoFeedsAdapter(
            viewModel.dataManager.getUser().userId?:return,
            list,
            object : ItemVideoFeedsViewModel.OnItemClickListener {
                override fun onClickClose() {
                    finish()
                }

                override fun onClickFind() {
                    startActivity(
                        intentProviderFactory.create(
                            VideoFeedSearchActivity::class.java,
                            null,
                            0
                        )
                    )
                }

                override fun onFollow(item: VideoData) {
                    viewModel.toggleFollowUser(item.createdBy.id)
                }

                override fun onOptionMenuClick(option: String, item: VideoData, position: Int) {
                    viewModel.onFeedOptionSelected(option, item, position)
                }

                override fun onLike(item: VideoData) {
                    viewModel.likeFeed(LIKES.THUMBS.type, item)
                }

                override fun onShare(item: VideoData) {

                }

                override fun onclickComment(item: VideoData, position: Int) {
                    val my = item.id

                    VideoFeedCommentBottomSheet.newInstance(
                        item.id,
                        position,
                        item.commentCount,
                        object : VideoFeedCommentBottomSheet.OnDismissClick {
                            override fun onItemClick(
                                videoFeedId: Int,
                                position: Int,
                                commentCount: Int
                            ) {
//                        val commentCount = it.data?.getIntExtra("commentCount",0)
//                        val position = it.data?.getIntExtra("index",0)
//                        println("$commentCount  POSITIONNNNN?>>>>$position")
                                viewModel.videoFeedList[position].commentCount = commentCount
                                adapter.notifyDataSetChanged()
                                //Toast.makeText(this@VideoFeedsActivity,commentCount.toString(),Toast.LENGTH_LONG).show()
                            }

                        }).show(supportFragmentManager, "CommentBottomSheet")

//                val intent = Intent(this@VideoFeedsActivity, VideoFeedCommentBottomSheet::class.java)
//                intent.putExtras( VideoFeedIdExtra.videoFeedIdExtra {
//                    feedId = item.id
//                    this.position =position
//                    commentCount= item.commentCount
//                })
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                getResult.launch(intent)

                }

                override fun onClickProfile(item: VideoData) {

                    startActivity(
                        intentProviderFactory.create(
                            VideoFeedProfileActivity::class.java,
                            VideoFeedUserExtras.videoFeedUserExtras {
                                userId = item.createdBy.id
                                followStatus = item.createdBy.following
                                userName = item.createdBy.name ?: ""
                                userImage = item.createdBy.profileImage ?: ""
                                dateTime = getFeedTime(item.createdAt.toLong())
                            },
                            0
                        )
                    )
                }

                override fun onClickCamera() {
                    requestPermissions(
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        getString(R.string.camera_permission_description),
                        CAMERA_PERMISSION_REQUEST
                    ) {
                        // openCameraToCaptureVideo()
                        startActivity(Intent(this@VideoFeedsActivity, CameraActivity::class.java))
                    }
                }
            })

        viewDataBinding.recyclerVideoFeeds.adapter = adapter
        //  viewDataBinding.recyclerVideoFeeds.layoutManager = LinearLayoutManager(this)
        addObserver(adapter)
    }

    private fun openCameraToCaptureVideo() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) { // First check if camera is available in the device
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(intent, CAMERA_PERMISSION_REQUEST);
        }
    }


    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == CommentsActivity.COMMENT_SCREEN) {
                val commentCount = it.data?.getIntExtra("commentCount", 0)
                val position = it.data?.getIntExtra("index", 0)
                println("$commentCount  POSITIONNNNN?>>>>$position")
                viewModel!!.videoFeedList.get(position!!).commentCount = commentCount!!
                adapter.notifyDataSetChanged()
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_PERMISSION_REQUEST) {
            if (intent?.data != null) {
                val uriPathHelper = URIPathHelper()
                val videoFullPath = uriPathHelper.getPath(
                    this,
                    intent.data!!
                ) // Use this video path according to your logic
                // if you want to play video just after recording it to check is it working (optional)
                if (videoFullPath != null) {
                    playVideoInDevicePlayer(videoFullPath);
                }
            }
        }
    }

    fun playVideoInDevicePlayer(videoPath: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoPath))
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4")
        startActivity(intent)
    }

    private fun addObserver(adapter: VideoFeedsAdapter) {
        val viewModel = viewModel ?: return
        viewModel.videoFeedLiveList.observe(this, Observer {
            adapter.setItems(it)
        })
    }

/*
    private fun showBottomSheet(
        isCancelable: Boolean
    ) {
        val view = LayoutInflater.from(this).inflate(
            R.layout.bottom_sheet_comment,
            findViewById(android.R.id.content),
            false
        )
        val addCommentButton=view.findViewById<ImageView>(R.id.ivSend)
        val recyclerComment = view.findViewById<RecyclerView>(R.id.recyclerComment)
        var list = arrayListOf<Comment>()
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        var list = arrayListOf<String>()
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")
//        list.add("1")

        var adapter = VideoFeedsCommentAdapter(list,
            object : ItemVideoFeedsCommentViewModel.OnItemClickListener {
                override fun replyComment(commentItem:Comment,replyMessage:String,commentIndex:Int) {
                    viewModel.parentId= commentItem.id
                    viewModel.onReply(replyMessage,commentIndex)
                }

                override fun likeComment(commentItem: Comment,position: Int) {
                }

                override fun onOptionMenuClick(
                    option: String,
                    commentItem: Comment,
                    position: Int
                ) {
                }
            },
            object : ItemReplyViewModel.OnReplyItemClickListener{
                override fun report(commentId: Int, item: Reply, index: Int) {
                }

                override fun likeReply(commentId: Int,item: Reply, index: Int,commentIndex:Int) {
                }

                override fun onOptionMenuClick(option: String, item: Reply, position: Int) {
                }
            }
        )

        recyclerComment.layoutManager = LinearLayoutManager(this)
        recyclerComment.adapter = adapter

        bottomSheet = BottomSheetDialog(this)
        bottomSheet.setCancelable(isCancelable)
        bottomSheet.setContentView(view)
        (view.parent as View).setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
        bottomSheet.show()

        bottomSheet.setOnDismissListener {
            bottomSheet = BottomSheetDialog(this)
            bottomSheet.dismiss()
        }

        bottomSheet.setOnCancelListener {
            bottomSheet = BottomSheetDialog(this)
            bottomSheet.dismiss()
        }

        addCommentButton.setOnClickListener {
            viewModel.onAddCommentClick()
        }
    }
*/


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults[0] == PermissionChecker.PERMISSION_GRANTED && grantResults[1] == PermissionChecker.PERMISSION_GRANTED) {
            startActivity(Intent(this@VideoFeedsActivity, CameraActivity::class.java))
        }

    }

    override fun onFeedLikeSuccess(feed: VideoData, position: Int) {
        adapter.updateLikedItem(feed, position)
    }

    override fun onOptionSelectedClick(videoFeed: VideoData, optionitem: String, position: Int) {

        if (optionitem == "Report Feed") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_feed))
                .setTitle(optionitem)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = videoFeed.id
                                option = optionitem
                            },
                            0
                        )
                    )
                }.build().show(this)
        }
        if (optionitem == "Report User") {
            ConfirmationCommonDialog.Builder()
                .setDescription(getString(R.string.do_you_want_to_report_this_feed))
                .setTitle(optionitem)
                .addPositiveListener {
                    startActivity(
                        intentProviderFactory.create(
                            SendReportActivity::class.java,
                            ReportExtras.reportExtras {
                                feedId = videoFeed.createdBy.id
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
                    viewModel?.toggleBlockUser(videoFeed.createdBy.id)
                }.build().show(this)
        }

    }

    override fun onFollowSuccess(userList: List<UserList>, position: Int) {
        adapter.updateFollowStatus(userList[0].following, viewModel.selectedItem)
    }


//    override fun onResume() {
//        super.onResume()
//      //  refreshList()
//    }
//    fun refreshList(){
//        viewModel?.clearList()
//        adapter.clearItems()
//        viewModel?.getPublishVideoList()
//    }
}