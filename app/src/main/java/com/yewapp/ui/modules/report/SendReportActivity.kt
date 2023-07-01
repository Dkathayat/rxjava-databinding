package com.yewapp.ui.modules.report


import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.databinding.ActivitySendReportBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.common.S3Upload
import com.yewapp.ui.modules.report.adapter.ReportMediaAdapter
import com.yewapp.ui.modules.report.navigator.SendReportNavigator
import com.yewapp.ui.modules.report.vm.ItemReportMediaViewModel
import com.yewapp.ui.modules.report.vm.SendReportViewModel
import com.yewapp.utils.CAMERA_PERMISSION_REQUEST
import com.yewapp.utils.FileUtils
import com.yewapp.utils.GALLERY_PERMISSION_REQUEST
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class SendReportActivity :
    BaseActivity<SendReportNavigator, SendReportViewModel, ActivitySendReportBinding>(),
    SendReportNavigator, AdapterView.OnItemSelectedListener, S3Upload.OnS3UploadListener {

    var imageFilePath = ""
    private lateinit var adapter: ReportMediaAdapter

    //Camera Image Result handler
    val takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            val imgFile = File(imageFilePath)
            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(applicationContext, imgFile)

                viewModel.isUploading.set(true)
               S3Upload.upload(
                    this@SendReportActivity,
                    Uri.fromFile(File(compressedImageFile.toURI())),
                    viewModel.awsCredentials,
                    this@SendReportActivity
                )
            }
             viewModel.isUploading.set(true)
            S3Upload.upload(this, Uri.fromFile(File(imageFilePath)), viewModel.awsCredentials,this)
        }
    }

    //Image Picker Result handler
    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val uri = it ?: return@registerForActivityResult
        GlobalScope.launch {
            val compressedImageFile = Compressor.compress(
                applicationContext,
                FileUtils.from(this@SendReportActivity, uri)
            )
            viewModel.isUploading.set(true)
            S3Upload.upload(
                this@SendReportActivity,
                Uri.fromFile(File(compressedImageFile.toURI())),
                viewModel.awsCredentials,
                this@SendReportActivity
            )
        }
        viewModel.isUploading.set(true)
        S3Upload.upload(this, uri, viewModel.awsCredentials,this)
    }


    override fun getLayout(): Int {
        return R.layout.activity_send_report
    }

    override fun init() {
        bind(SendReportViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SendReportViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySendReportBinding) {

        val list = mutableListOf<String>()
        adapter = ReportMediaAdapter(list, object : ItemReportMediaViewModel.OnItemClickListener {

            override fun onClickItem(item: String, index: Int) {

            }

            override fun onCrossClick(item: String) {
                viewModel.list.remove(item)
                addObserver()
            }
        })
        adapter.setHasStableIds(true)
        viewDataBinding.recyclerView.adapter = adapter
        viewDataBinding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.listLive.observe(this, Observer {
            // adapter.setItems(it)
        })
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

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        val item = parent?.getItemAtPosition(pos) as String
        viewModel.reasonText.set(item)

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun uploadValidation(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }




    override fun onSendReportSuccess(message: String) {
        onSuccess(message)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            finish()
        }
    }

    //Callback to check if the User has clicked Image or Gallery as Image picking option


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
        viewModel.dropboxIconVisibility.set(false)
        viewModel.addImages(url)
    }

    override fun onS3UploadVideoClicked(url: String, type: String) {
    }


}


