package com.yewapp.ui.modules.addassociatememberdetails.fragment.sportsimages

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.yewapp.R
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.databinding.FragmentAssociateSportsImagesBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.common.RemoveAccountBottomSheet
import com.yewapp.ui.common.S3Upload
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsActivity
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsViewModel
import com.yewapp.ui.modules.editProfile.SportTypeActivity
import com.yewapp.ui.modules.profilesubsportstype.ProfileSubSportsTypeActivity
import com.yewapp.ui.modules.profilesubsportstype.extras.ProfileSubSportsTypeExtras
import com.yewapp.ui.modules.subscription.SubscriptionActivity
import com.yewapp.utils.CAMERA_PERMISSION_REQUEST
import com.yewapp.utils.FileUtils
import com.yewapp.utils.GALLERY_PERMISSION_REQUEST
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*

class AssociateSportsImagesFragment(override val layoutId: Int = R.layout.fragment_associate_sports_images) :
    BaseFragment<AssociateSportsImagesNavigator, AssociateSportsImagesViewModel, FragmentAssociateSportsImagesBinding>(),
    AssociateSportsImagesNavigator, EasyPermissions.PermissionCallbacks,
    S3Upload.OnS3UploadListener {

    lateinit var adapter: AssociateSportsTypeAdapter
    lateinit var addMembersDetailsViewModel: AddMembersDetailsViewModel
    var imageFilePath = ""
    var imageUri: Uri? = null


    private val cropResultContracts = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity(imageUri)
//                .setGuidelines(CropImageView.Guidelines.OFF)
                .getIntent(requireContext())
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


    companion object {
        fun newInstance(extras: Bundle): AssociateSportsImagesFragment {
            //can not send without bundle else will give null pointer error
            val fragment = AssociateSportsImagesFragment()
//            val args = Bundle()

            fragment.arguments = extras
            return fragment
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if (!viewModel?.associateId?.get().equals("") && !viewModel?.isLoading?.get()!!)
//            viewModel?.getAssociateSportType(viewModel?.associateId?.get() ?: return)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return bind(AssociateSportsImagesViewModel::class.java, inflater, container)
    }


    override fun onViewModelCreated(viewModel: AssociateSportsImagesViewModel) {
        viewModel.setNavigator(this)
        addMembersDetailsViewModel =
            ViewModelProvider(requireActivity())[AddMembersDetailsViewModel::class.java]
    }

    override fun onViewBound(viewDataBinding: FragmentAssociateSportsImagesBinding) {
        adapter =
            AssociateSportsTypeAdapter(mutableListOf(),
                object : ItemAssociateSportTypeViewModel.OnItemClickListener {
                    override fun onAddItem(sportType: SportType, position: Int) {
                        if (sportType.isChecked ?: false)
                            viewModel?.isNextActive?.set(true)
                        else
                            viewModel?.isNextActive?.set(false)

                        var count = 0
                        for (i in 0 until viewModel?.associateSportTypeList?.size!!) {
                            if (viewModel?.associateSportTypeList!![i].isModelAdded != true) {
                                if (position == i && sportType.isChecked ?: false) {
                                    sportType.isChecked = true
                                    viewModel?.associateSportTypeList?.set(i, sportType)
                                    viewModel?.selectedSportsType = sportType
                                    count++
                                } else {
                                    viewModel?.associateSportTypeList!![i].isChecked = false
                                    viewModel?.associateSportTypeList?.set(
                                        i,
                                        viewModel?.associateSportTypeList!![i]
                                    )
                                }
                            }
                        }
                        if (count == 0) {
                            viewModel?.isSaveImages?.set(false)
                        } else viewModel?.isSaveImages?.set(true)

                        viewModel?.associateSportTypeMutableList?.value =
                            viewModel?.associateSportTypeList

                    }

//                    override fun onRemoveItem(sportType: SportType, position: Int) {
//                        viewModel?.mutableSportsList?.set(position, sportType)
//                        adapter.notifyDataSetChanged()
//
//                    }

                    override fun addSportsType(sportType: SportType, position: Int) {
//                        navigateToAddSubSportsType(sportType)
                    }

                    override fun editSportsType(sportType: SportType, position: Int) {
                        navigateToEditSubSportsType(sportType)
                    }

                    override fun deleteSportsType(sportType: SportType, position: Int) {
                        //used to delete sports data
                        val title = viewModel?.dataManager?.getResourceProvider()
                            ?.getString(R.string.delete_sport)

                        val message = viewModel?.dataManager?.getResourceProvider()
                            ?.getString(R.string.remove_sport_type_message)
                        RemoveAccountBottomSheet.Builder()
                            .setTitle(title ?: return)
                            .setMessage(message ?: return)
                            .setButtonRemove {
                                viewModel?.deleteSportsData(sportType.sportId)
                            }
                            .build().show(requireActivity())
                    }

                })
        adapter.setHasStableIds(true)
        viewDataBinding.rvSportType.adapter = adapter

        fetchSportTypes()


        //TODO: Register crop image launcher
        cropImageResultLauncher = registerForActivityResult(cropResultContracts) {
            //  Received cropped image result both gallery and camera and upload cropped image on AWS server
            imageUri = it
            if (it != null) {
                viewModel?.isLoading?.set(true)
                S3Upload.upload(
                    requireContext(),
                    imageUri ?: return@registerForActivityResult,
                    viewModel?.awsCredentials ?: return@registerForActivityResult,
                    this
                )
            }
        }
    }

    private fun fetchSportTypes() {
//        viewModel?.getAssociateSportType()
        addObserver()
    }

    private fun addObserver() {
        viewModel?.associateSportTypeLiveDataList?.observe(this, Observer {
            adapter.addItems(it)
        })

    }

    override fun navigateToSportType() {
        if (addMembersDetailsViewModel.associateID.get() == 0) {
            Toast.makeText(
                requireContext(), "Please add associate profile details first.", Toast.LENGTH_SHORT
            ).show()
            return
        }

        startActivity(
            intentProviderFactory.create(
                SportTypeActivity::class.java, null, 0
            )
        )
    }

    override fun onBackPress() {

    }


    override fun launchImageOptions() {
        GenericListBottomSheet.Builder<String>().setTitleText(getString(R.string.choose_from))
            .setDataList(mutableListOf("Camera", "Gallery")).setClickListener {
                checkImageOption(it)
            }.build().show(requireActivity())
    }

    override fun navigateToAddSubSportsType() {
        if (viewModel?.selectedSportsType?.isModelAdded ?: return) {
            Toast.makeText(requireContext(), "you can only edit this sports", Toast.LENGTH_SHORT)
                .show()
        } else {
            val extras = ProfileSubSportsTypeExtras.addSportsDataExtras {
                this.isEdit = false
                this.associateID = viewModel?.associateId?.get() ?: ""
                this.profileImage = viewModel?.profileImage?.get() ?: ""
                this.profileCoverImage = viewModel?.profileCoverImageURL?.get() ?: ""
                this.sportType = viewModel?.selectedSportsType ?: return
            }

            startActivity(
                intentProviderFactory.create(
                    ProfileSubSportsTypeActivity::class.java,
                    extras,
                    0
                )
            )
        }
    }


//    override fun saveSportsSucess(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }

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

    private fun launchCamera() {
        val file = FileUtils.createImageFile(requireContext())
        val fileUri = FileProvider.getUriForFile(
            requireContext(), "com.yew.fileprovider", file.second
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
        if (viewModel?.isProfileImageClicked?.get()!!) {
            viewModel?.profileImage?.set(url)
//            viewModel.bannerAddIconVisibility.set(false)
        } else {
            viewModel?.profileCoverImageURL?.set(url)
//            viewModel.challengeAddIconVisibility.set(false)
        }
        viewModel?.isLoading?.set(false)

//        if (viewModel?.validate() ?: return)
//            viewModel.isContinueActive.set(true)
    }

    override fun onS3UploadVideoClicked(url: String, type: String) {
    }


    private fun navigateToEditSubSportsType(sportType: SportType) {
        val extras = ProfileSubSportsTypeExtras.addSportsDataExtras {
            this.isEdit = true
            this.associateID = viewModel?.associateId?.get() ?: ""
            this.profileImage = viewModel?.profileImage?.get() ?: ""
            this.profileCoverImage = viewModel?.profileCoverImageURL?.get() ?: ""
            this.sportType = sportType
        }

        startActivity(
            intentProviderFactory.create(
                ProfileSubSportsTypeActivity::class.java,
                extras,
                0
            )
        )
    }

}