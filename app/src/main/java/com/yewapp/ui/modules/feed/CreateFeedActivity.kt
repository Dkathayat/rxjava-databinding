package com.yewapp.ui.modules.feed

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.databinding.ActivityCreateFeedBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.common.S3Upload
import com.yewapp.ui.modules.feed.adapter.FeedsImageAdapter
import com.yewapp.ui.modules.feed.navigator.CreateFeedNavigator
import com.yewapp.ui.modules.feed.vm.CreateFeedImageItemViewModel
import com.yewapp.ui.modules.feed.vm.CreateFeedViewModel
import com.yewapp.utils.*
import id.zelory.compressor.Compressor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*

class CreateFeedActivity :
    BaseActivity<CreateFeedNavigator, CreateFeedViewModel, ActivityCreateFeedBinding>(),
    CreateFeedNavigator, EasyPermissions.PermissionCallbacks, S3Upload.OnS3UploadListener {

    var imageFilePath = ""
    private lateinit var adapter: FeedsImageAdapter

    //Video Picker Result handling
    private val pickVideo = registerForActivityResult(GetContent()) {
        val uri = it ?: return@registerForActivityResult
        viewModel.isUploading.set(true)
        S3Upload.uploadVideo(VIDEO, this, uri, viewModel.awsCredentials, this)
    }

    //Camera Image Result handler
    private val takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            val imgFile = File(imageFilePath)
            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(applicationContext, imgFile)

                viewModel.isUploading.set(true)
                S3Upload.upload(
                    this@CreateFeedActivity,
                    Uri.fromFile(File(compressedImageFile.toURI())),
                    viewModel.awsCredentials,
                    this@CreateFeedActivity
                )
            }
//            viewModel.isUploading.set(true)
//            S3Upload.upload(this, Uri.fromFile(File(imageFilePath)), this)
        }
    }

    //Image Picker Result handler
    private val pickImage = registerForActivityResult(GetContent()) {
        val uri = it ?: return@registerForActivityResult
        GlobalScope.launch {
            val compressedImageFile = Compressor.compress(
                applicationContext,
                FileUtils.from(this@CreateFeedActivity, uri)
            )
            viewModel.isUploading.set(true)
            S3Upload.upload(
                this@CreateFeedActivity,
                Uri.fromFile(File(compressedImageFile.toURI())),
                viewModel.awsCredentials,
                this@CreateFeedActivity
            )
        }
//        viewModel.isUploading.set(true)
//        S3Upload.upload(this, uri, this)
    }


    override fun getLayout(): Int = R.layout.activity_create_feed

    override fun init() {
        bind(CreateFeedViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: CreateFeedViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityCreateFeedBinding) {
        val list = mutableListOf<String>()

        adapter =
            FeedsImageAdapter(list, object : CreateFeedImageItemViewModel.OnItemClickListener {
                override fun onClickItem(item: String) {
                }

                override fun onCrossClick(item: String) {
                    viewModel._list.remove(item)
                    addObserver()

                }

            })
        adapter.setHasStableIds(true)
        viewDataBinding.imagerecyclerview.adapter = adapter
        viewDataBinding.imagerecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.listLive.observe(this, androidx.lifecycle.Observer {
            this.adapter.setItems(it)
        })
    }

    override fun closeActivity() {
        finish()
    }

    override fun chooseActivity() {

    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    override fun launchImageOptions() {
        GenericListBottomSheet.Builder<String>()
            .setTitleText(getString(R.string.choose_from))
            .setDataList(mutableListOf("Camera", "Gallery"))
            .setClickListener {
                checkImageOption(it)
            }.build().show(this)
    }

    //Callback to check if the User has clicked Image or Gallery as Image picking option
    private fun checkImageOption(it: String) {
        when (it.lowercase(Locale.getDefault())) {
            "camera" -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    getString(R.string.camera_permission_description),
                    CAMERA_PERMISSION_REQUEST
                ) {
                    launchCamera()
                }
            }
            "gallery" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                        getString(R.string.gallery_permission_description),
                        GALLERY_PERMISSION_REQUEST
                    ) {
                        pickImage.launch("image/*")
                    }
                } else {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        getString(R.string.gallery_permission_description),
                        GALLERY_PERMISSION_REQUEST
                    ) {
                        pickImage.launch("image/*")
                    }
                }

//                requestPermissions(
//                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                    getString(R.string.gallery_permission_description),
//                    GALLERY_PERMISSION_REQUEST
//                ) {
//                    pickImage.launch("image/*")
//                }
            }
        }
    }

    override fun launchVideoPicker() {
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            getString(R.string.video_permission_rationale),
            STORAGE_PERMISSION_REQUEST
        ) {
            pickVideo.launch("video/*")
        }
    }

    override fun uploadValidation(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    fun launchCamera() {
        val file = FileUtils.createImageFile(this)
        val fileUri = FileProvider.getUriForFile(
            this,
            "com.yew.fileprovider",
            file.second
        )
        imageFilePath = file.first
        takeImage.launch(fileUri)
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST -> {
                pickVideo.launch("video/*")
            }
            CAMERA_PERMISSION_REQUEST -> {
                launchCamera()
            }
            GALLERY_PERMISSION_REQUEST -> {
                pickImage.launch("image/*")
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        super.onPermissionsDenied(requestCode, perms)
    }

    override fun onS3UploadClicked(url: String) {
        viewModel.isUploading.set(false)
        viewModel._list.add(url)
        viewModel.list.value = viewModel._list
    }

    override fun onS3UploadVideoClicked(url: String, type: String) {
        viewModel._videoList.add(url)
        viewModel.videoList.value = viewModel._videoList
        viewModel.isUploading.set(false)
        if (viewModel._videoList.size == 1) {
            viewModel.videoImage1.set(url)
            viewModel.videoImage1Status.set(true)
        }
        if (viewModel._videoList.size == 2) {
            viewModel.videoImage2.set(url)
            viewModel.videoImage2Status.set(true)
        }
        if (viewModel._videoList.size == 3) {
            viewModel.videoImage3.set(url)
            viewModel.videoImage3Status.set(true)
        }

    }

}