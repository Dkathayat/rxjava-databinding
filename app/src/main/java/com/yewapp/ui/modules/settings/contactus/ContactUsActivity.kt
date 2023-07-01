package com.yewapp.ui.modules.settings.contactus

import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.yewapp.R
import com.yewapp.databinding.ActivityContactUsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.common.S3Upload
import com.yewapp.utils.CAMERA_PERMISSION_REQUEST
import com.yewapp.utils.FileUtils
import com.yewapp.utils.GALLERY_PERMISSION_REQUEST
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class ContactUsActivity :
    BaseActivity<ContactUsNavigator, ContactUsViewModel, ActivityContactUsBinding>(),
    ContactUsNavigator, S3Upload.OnS3UploadListener {
    var imageFilePath = ""

    //Camera Image Result handler
    val takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            val imgFile = File(imageFilePath)
            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(applicationContext, imgFile)

                viewModel.isUploading.set(true)
                S3Upload.upload(
                    this@ContactUsActivity,
                    Uri.fromFile(File(compressedImageFile.toURI())),
                    viewModel.awsCredentials,
                    this@ContactUsActivity
                )
            }
//            viewModel.isUploading.set(true)
//            S3Upload.upload(this, Uri.fromFile(File(imageFilePath)), this)
        }
    }

    //Image Picker Result handler
    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val uri = it ?: return@registerForActivityResult

        //   val imgFile = File(FileUtils.getRealPathFromURI(this@ContactUsActivity,uri).toString())
        GlobalScope.launch {
            val compressedImageFile =
                Compressor.compress(applicationContext, FileUtils.from(this@ContactUsActivity, uri))
            viewModel.isUploading.set(true)
            S3Upload.upload(
                this@ContactUsActivity,
                Uri.fromFile(File(compressedImageFile.toURI())),
                viewModel.awsCredentials,
                this@ContactUsActivity
            )
        }
//        viewModel.isUploading.set(true)
//        S3Upload.upload(this, uri, this)
    }


    override fun getLayout(): Int = R.layout.activity_contact_us

    override fun init() {
        bind(ContactUsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ContactUsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityContactUsBinding) {

    }

    override fun onContactSuccess(message: String) {
        onSuccess(message)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            finish()
        }
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
        viewModel.attachedImage.set(url)
        viewModel.dropboxIconVisibility.set(false)
    }

    override fun onS3UploadVideoClicked(url: String, type: String) {
    }

}