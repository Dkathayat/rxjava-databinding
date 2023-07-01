package com.yewapp.ui.modules.addchallenge.challengebanner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.ActivityChallengeBannerBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.common.S3Upload
import com.yewapp.ui.modules.addchallenge.invitemember.InviteMemberActivity
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.CAMERA_PERMISSION_REQUEST
import com.yewapp.utils.FileUtils
import com.yewapp.utils.GALLERY_PERMISSION_REQUEST
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*

//Todo:Add Challenge Step 8
class ChallengeBannerActivity :
    BaseActivity<ChallengeBannerNavigator, ChallengeBannerViewModel, ActivityChallengeBannerBinding>(),
    ChallengeBannerNavigator, EasyPermissions.PermissionCallbacks, S3Upload.OnS3UploadListener {

    private var imageFilePath = ""
    var imageUri: Uri? = null

    private val cropResultContracts = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity(imageUri)
//                .setGuidelines(CropImageView.Guidelines.OFF)
                .getIntent(this@ChallengeBannerActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private lateinit var cropImageResultLauncher: ActivityResultLauncher<Any?>

    //Image Picker Result handler
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val uri = it ?: return@registerForActivityResult
        imageUri = uri
        //Call Crop image launch
        cropImageResultLauncher.launch(null)
    }

    //Camera Image Result handler
    private val takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            imageUri = Uri.fromFile(File(imageFilePath))
            //Call Crop image launch
            cropImageResultLauncher.launch(null)
        }
    }


    override fun getLayout(): Int {
        return R.layout.activity_challenge_banner
    }

    override fun init() {
        bind(ChallengeBannerViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ChallengeBannerViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChallengeBannerBinding) {
//        Repository.getInstance().getChallengeProgress().observe(this) {
//            viewModel.challengeExtras = it
//        }

        //TODO: Register crop image launcher
        cropImageResultLauncher = registerForActivityResult(cropResultContracts) {
            //  Received cropped image result both gallery and camera and upload cropped image on AWS server
            imageUri = it
            if (it != null) {
                viewModel.isLoading.set(true)
                S3Upload.upload(
                    this,
                    imageUri ?: return@registerForActivityResult,
                    viewModel.awsCredentials ?: return@registerForActivityResult,
                    this
                )
            }
        }
    }


    override fun navigateToInviteMemberScreen() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                8,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                2,//for popular route
                viewModel.challengeModel.routeID,
                viewModel.challengeModel.challengeName,//challengeName
                viewModel.challengeModel.challengeVisibility,//challenge visibility
                viewModel.challengeModel.challengeStatus,//challenge visibility
                viewModel.challengeModel.selectedSportsLevel,//challenge selectedSportsLevel
                viewModel.challengeModel.ageGroup,//challenge selectedAgeGroupValue
                viewModel.challengeModel.challengeDescription,//challenge description
                viewModel.challengeModel.subSportTypeId,
                viewModel.challengeModel.sportsEquipments,
                //step-5 A (for radius reach)
                viewModel.challengeModel.challengeArea,//radius reach / Extended
                viewModel.challengeModel.location,//radius reach / Extended
                viewModel.challengeModel.latitude,
                viewModel.challengeModel.longitude,
                viewModel.challengeModel.radius,

                //step-5 B (Extended reach)
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                viewModel.challengeModel.cityId,
                viewModel.challengeModel.zipCode,
                //step-6
                viewModel.challengeModel.calories,
                viewModel.challengeModel.miles,
                viewModel.challengeModel.elevationGain,
                viewModel.challengeModel.avgWatt,
                viewModel.challengeModel.time,
                viewModel.challengeModel.maxMember,

                //Step 7
                viewModel.challengeModel.winnerPickedFrom,
                viewModel.challengeModel.selectedWinnerPrize,
                viewModel.challengeModel.overViewValue,
                viewModel.challengeModel.winnerValue,
                viewModel.challengeModel.additionalInfoValue,
                viewModel.challengeModel.rulesValue,
                viewModel.challengeModel.guidelinesValue,
                viewModel.challengeModel.qualificationValue,
                //STEP 8
                viewModel.bannerImage.get(),
                viewModel.challengeImage.get(),
                arrayListOf<InviteMember>()
            )
        }
        startActivity(
            intentProviderFactory.create(
                InviteMemberActivity::class.java, extras, 0
            )
        )
    }

    override fun navigateToEditInviteMemberScreen() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                8,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                2,//for popular route
                viewModel.challengeModel.routeID,
                viewModel.challengeModel.challengeName,//challengeName
                viewModel.challengeModel.challengeVisibility,//challenge visibility
                viewModel.challengeModel.challengeStatus,//challenge visibility
                viewModel.challengeModel.selectedSportsLevel,//challenge selectedSportsLevel
                viewModel.challengeModel.ageGroup,//challenge selectedAgeGroupValue
                viewModel.challengeModel.challengeDescription,//challenge description
                viewModel.challengeModel.subSportTypeId,
                viewModel.challengeModel.sportsEquipments,
                //step-5 A (for radius reach)
                viewModel.challengeModel.challengeArea,//radius reach / Extended
                viewModel.challengeModel.location,//radius reach / Extended
                viewModel.challengeModel.latitude,
                viewModel.challengeModel.longitude,
                viewModel.challengeModel.radius,

                //step-5 B (Extended reach)
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                viewModel.challengeModel.cityId,
                viewModel.challengeModel.zipCode,
                //step-6
                viewModel.challengeModel.calories,
                viewModel.challengeModel.miles,
                viewModel.challengeModel.elevationGain,
                viewModel.challengeModel.avgWatt,
                viewModel.challengeModel.time,
                viewModel.challengeModel.maxMember,

                //Step 7
                viewModel.challengeModel.winnerPickedFrom,
                viewModel.challengeModel.selectedWinnerPrize,
                viewModel.challengeModel.overViewValue,
                viewModel.challengeModel.winnerValue,
                viewModel.challengeModel.additionalInfoValue,
                viewModel.challengeModel.rulesValue,
                viewModel.challengeModel.guidelinesValue,
                viewModel.challengeModel.qualificationValue,
                //STEP 8
                viewModel.bannerImage.get(),
                viewModel.challengeImage.get(),
                viewModel.challengeModel.inviteMembers
            )
        }
        startActivity(
            intentProviderFactory.create(
                InviteMemberActivity::class.java, extras, 0
            )
        )
    }

    override fun launchImageOptions() {
        GenericListBottomSheet.Builder<String>().setTitleText(getString(R.string.choose_from))
            .setDataList(mutableListOf("Camera", "Gallery")).setClickListener {
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
            }

        }
    }

    private fun launchCamera() {
        val file = FileUtils.createImageFile(this)
        val fileUri = FileProvider.getUriForFile(
            this, "com.yew.fileprovider", file.second
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
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
        viewModel.isLoading.set(false)
        if (viewModel.bannerIcon.get()) {
            viewModel.bannerImage.set(url)
            viewModel.bannerAddIconVisibility.set(false)
        } else {
            viewModel.challengeImage.set(url)
            viewModel.challengeAddIconVisibility.set(false)
        }
        if (viewModel.validate())
            viewModel.isContinueActive.set(true)
    }

    override fun onS3UploadVideoClicked(url: String, type: String) {
    }
}