package com.yewapp.ui.modules.feedback.fragment

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.yewapp.R
import com.yewapp.databinding.FragmentBugFixesBinding
import com.yewapp.databinding.FragmentShareFeedbackBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.common.S3Upload
import com.yewapp.ui.modules.feedback.navigator.ShareFeedbackFragmentNavigator
import com.yewapp.ui.modules.feedback.vm.BugFixesViewModel
import com.yewapp.ui.modules.feedback.vm.ShareFeedbackFragmentViewModel
import com.yewapp.utils.CAMERA_PERMISSION_REQUEST
import com.yewapp.utils.FileUtils
import com.yewapp.utils.GALLERY_PERMISSION_REQUEST
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class BugFixesFragment(override val layoutId: Int = R.layout.fragment_bug_fixes) :
    BaseFragment<ShareFeedbackFragmentNavigator, BugFixesViewModel, FragmentBugFixesBinding>(),
    ShareFeedbackFragmentNavigator, S3Upload.OnS3UploadListener {
    var imageFilePath = ""

    private val takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            val imgFile = File(imageFilePath)
            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(activity!!.applicationContext, imgFile)

                viewModel?.isUploading?.set(true)
                S3Upload.upload(
                    requireContext(),
                    Uri.fromFile(File(compressedImageFile.toURI())),
                    viewModel!!.awsCredentials,
                    this@BugFixesFragment
                )
            }
            viewModel?.isUploading?.set(true)
            S3Upload.upload(requireContext(), Uri.fromFile(File(imageFilePath)), viewModel!!.awsCredentials,this)
        }
    }
    override fun onViewModelCreated(viewModel: BugFixesViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentBugFixesBinding) {
        viewModel?.getFeedbackList()

    }

    override fun launchImageOptions() {
        GenericListBottomSheet.Builder<String>()
            .setTitleText(getString(R.string.choose_from))
            .setDataList(mutableListOf("Camera", "Gallery"))
            .setClickListener {
                checkImageOption(it)
            }.build().show(requireActivity())
    }

    override fun onSendReportSuccess(message: String) {
        onSuccess(message)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            activity?.finish()
        }
    }

    override fun uploadValidation(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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

    val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val uri = it ?: return@registerForActivityResult
        GlobalScope.launch {
            val compressedImageFile = Compressor.compress(
                activity!!.applicationContext,
                FileUtils.from(requireContext(), uri)
            )
            viewModel?.isUploading?.set(true)
            S3Upload.upload(
                requireContext(),
                Uri.fromFile(File(compressedImageFile.toURI())),
                viewModel!!.awsCredentials,
                this@BugFixesFragment
            )
        }
        viewModel?.isUploading?.set(true)
        S3Upload.upload(requireContext(), uri, viewModel!!.awsCredentials,this)
    }

    private fun launchCamera() {
        val file = FileUtils.createImageFile(requireContext())
        val fileUri = FileProvider.getUriForFile(
            requireContext(),
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
    override fun onBackPress() {

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(BugFixesViewModel::class.java,inflater,container)
    }

    override fun onS3UploadClicked(url: String) {
        viewModel?.isUploading?.set(false)
        viewModel?.dropboxIconVisibility?.set(false)
        viewModel?.addImages(url)
        viewModel?.attachedImage?.set(true)
        viewModel?.uploadedImg?.set(url)
    }

    override fun onS3UploadVideoClicked(url: String, type: String) {


    }
}