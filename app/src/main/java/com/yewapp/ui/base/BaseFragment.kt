package com.yewapp.ui.base


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Spannable
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.yewapp.BR
import com.yewapp.R
import com.yewapp.ui.dialogs.CompleteProfileDialog
import com.yewapp.ui.dialogs.PermissionsDialog
import com.yewapp.ui.modules.editProfile.EditProfileActivity
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.login.LoginActivity
import com.yewapp.ui.modules.settings.setting.SettingsActivity
import com.yewapp.ui.modules.signup.SignUpOptionsActivity
import com.yewapp.utils.factory.IntentProviderFactory
import com.yewapp.utils.factory.ViewModelProviderFactory
import com.yewapp.utils.httpErrorMap
import com.yewapp.utils.mask
import com.yewapp.utils.showError
import com.yewapp.utils.showSuccess
import dagger.android.support.AndroidSupportInjection
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


abstract class BaseFragment<N, VM : BaseViewModel<N>, VB : ViewDataBinding> : Fragment(),
    View.OnClickListener, BaseNavigator, EasyPermissions.PermissionCallbacks {
    protected abstract val layoutId: Int

    lateinit var viewDataBinding: VB
    var viewModel: VM? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    @Inject
    lateinit var intentProviderFactory: IntentProviderFactory

    abstract fun onViewModelCreated(viewModel: VM)
    abstract fun onViewBound(viewDataBinding: VB)
    var bottomSheet: BottomSheetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onDetach() {
        super.onDetach()
    }

    private fun <T : BaseViewModel<N>> getViewModel(viewModelClass: Class<T>): T {

        //Use 'this' in case of separate ViewModels
        //User 'requireActivity()' in case of SharedViewModel
        return ViewModelProvider(requireActivity(), viewModelFactory!!).get(viewModelClass)
        //return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }

    //Hides the Keyboard when the User clicks on the screen
    fun hideKeyboard() {
        val currentView = requireView()
        if (currentView != null) {
            currentView.clearFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentView.windowToken, 0)
        }
    }


    fun bind(viewModelClass: Class<VM>, inflater: LayoutInflater, container: ViewGroup?): View {
        viewModel = if (viewModel == null) getViewModel(viewModelClass) else viewModel
        onViewModelCreated(viewModel!!)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding.setVariable(BR.viewModel, viewModel)
        viewDataBinding.executePendingBindings()
        onViewBound(viewDataBinding)
        viewModel!!.setData(arguments)
        return viewDataBinding.root
    }

    companion object {
        const val PERMISSION_CAMERA = 10001
        const val PERMISSION_WRITE_EXTREANL = 10002

        const val CAMERA = 11001
        const val GALLERY = 11002

        const val PIC_FROM_GALLERY = 0
        const val PIC_FROM_CAMERA = 1

    }

    override fun onError(t: Throwable, showAction: Boolean, function: () -> Unit) {
        if (t is HttpException) {
            when (val code = t.code()) {
                401 -> {
                    activity?.showError(httpErrorMap[code] ?: return, showAction = false)
                    Handler().postDelayed({

                        onSessionExpired()
                    }, 2000)
                }
                else -> activity?.showError(httpErrorMap[code] ?: return, showAction = true) {
                    function()
                }
            }

        } else {
            activity?.showError(t.message ?: return, showAction = showAction) {
                function()
            }
        }
    }

    override fun onSuccess(message: String, showAction: Boolean, function: () -> Unit) {
        activity?.showSuccess(message, showAction, function)
    }

    fun onSessionExpired() {
        //Call activity on Session Expire
        FirebaseAuth.getInstance().signOut()
        FirebaseAuth.getInstance().signOut()
        startActivity(
            intentProviderFactory.create(
                LoginActivity::class.java,
                null,
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
        viewModel?.clearFlags()
        activity?.finish()
    }

    override fun onClick(v: View?) {

    }

    override fun navigateToSetting() {
        startActivity(
            intentProviderFactory.create(
                SettingsActivity::class.java,
                null,
                0
            )
        )
    }


    fun getImageFile(data: ByteArray, name: String): File {
        val file = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), name)
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.use { it.write(data) }

        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotatedBitmap = rotateIfRequired(bitmap, file.path)
        val cropped = cropToCenter(rotatedBitmap)
        /*val compressed = compressImageSize(cropped, 1280)*/

        val finalFile =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), name)
        FileOutputStream(finalFile).use {
            cropped.compress(Bitmap.CompressFormat.JPEG, 80, it)
        }

        return file
    }

    private fun rotateIfRequired(bitmap: Bitmap, path: String): Bitmap {
        val ei = ExifInterface(path)

        return when (ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun cropToCenter(bitmap: Bitmap) =
        if (bitmap.width >= bitmap.height) {
            val x = (bitmap.width / 4 - bitmap.height / 4)
            Bitmap.createBitmap(bitmap, x, 0, bitmap.height, bitmap.height)
        } else {
            val y = (bitmap.height / 4 - bitmap.width / 4)
            Bitmap.createBitmap(bitmap, 0, y, bitmap.width, bitmap.width)
        }

    /**
     * @author Muheeb Mehraj
     * @param required Permission details, Permission Description
     * @description Used to request multiple permissions at once which are handled by Easy Permssion
     * library.
     */
    fun requestPermissions(
        rcList: Array<String>,
        reqRational: String,
        code: Int,
        afterTask: () -> Unit
    ) {
        if (EasyPermissions.hasPermissions(requireContext(), *rcList)) {
            afterTask()
        } else {
            EasyPermissions.requestPermissions(this, reqRational, code, *rcList)
        }
    }

    /**
     * @author Muheeb Mehraj
     * @description Callback invoked after the user performs any action on Permission Dialog
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //Shows the dialog which redirects the user to App Settings where User can allow the
            // permission
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    fun afterPermissionTask() {
        Toast.makeText(requireContext(), "Permission exists", Toast.LENGTH_LONG).show()
    }

    //-------------------------------------------------------------------------------------------------------PERMISSION
    fun checkCameraPermission(
        permission: Array<String>,
        requestCode: Int
    ): Boolean {
        return when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                permission[0]
            ) == PackageManager.PERMISSION_GRANTED -> {
                true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission[0]
            ) -> {
                PermissionsDialog.Builder()
                    .setTitle(getString(R.string.camera_permission_description))
                    .setDescription(getString(R.string.camera_permission_description))
                    .setNegativeText(getString(R.string.app_settings))
                    .setPositiveText(getString(R.string.not_now))
                    .build()
                    .show(requireActivity())
                false
            }
            else -> {
                requestGeneralPermission(permission, requestCode)
                false
            }
        }
    }


    fun checkGalleryPermission(
        permission: Array<String>,
        requestCode: Int
    ): Boolean {
        return when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                permission[0]
            ) == PackageManager.PERMISSION_GRANTED -> {
                true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission[0]
            ) -> {
                PermissionsDialog.Builder()
                    .setTitle(getString(R.string.gallery_permission_title))
                    .setDescription(getString(R.string.gallery_permission_description))
                    .setNegativeText(getString(R.string.app_settings))
                    .setPositiveText(getString(R.string.not_now))
                    .build()
                    .show(requireActivity())
                false
            }
            else -> {
                requestGeneralPermission(permission, requestCode)
                false
            }
        }
    }

    fun setSpannableTextWithColor(
        view: TextView,
        resourceId: Int,
        email: String,
        userColor: Int
    ) {
        val phone = email.mask(getMaskLength(email))
        val fulltext = getString(resourceId, phone)
        view.setText(fulltext, TextView.BufferType.SPANNABLE)
        val str = view.text as Spannable
        val emailIndex = fulltext.indexOf(phone)
        str.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(
                        view.context,
                        userColor
                    );
                    ds.typeface = Typeface.DEFAULT_BOLD
                }
            }, emailIndex, emailIndex + email.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }


    private fun getMaskLength(email: String): Int {
        val length = email.split("@")[0].length
        return length * 2 / 3
    }

    private fun requestGeneralPermission(permission: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(
            requireActivity(),
            permission,
            requestCode
        )
    }

    override fun showProfileCompletionAlert(message: String) {
        CompleteProfileDialog.Builder()
            .setDescription(getString(R.string.complete_profile))
            .setClickListener {
                startActivity(
                    intentProviderFactory.create(
                        EditProfileActivity::class.java,
                        EditProfileExtras.editProfileExtras {
                            isSignUp = true
                        },
                        0
                    )
                )
            }
            .build()
            .show(activity ?: return)
    }
}


