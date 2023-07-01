package com.yewapp.ui.base

//import com.amazonaws.util.IOUtils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.yewapp.BR
import com.yewapp.R
import com.yewapp.ui.dialogs.CompleteProfileDialog
import com.yewapp.ui.modules.editProfile.EditProfileActivity
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.settings.setting.SettingsActivity
import com.yewapp.ui.modules.signup.SignUpOptionsActivity
import com.yewapp.utils.*
import com.yewapp.utils.factory.IntentProviderFactory
import com.yewapp.utils.factory.ViewModelProviderFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

abstract class BaseActivity<N, V : BaseViewModel<N>, VB : ViewDataBinding> : AppCompatActivity(),
    View.OnClickListener, EasyPermissions.PermissionCallbacks, BaseNavigator, HasAndroidInjector {

    private val googleSignInClient: GoogleSignInClient?=null
    lateinit var bottomSheet: BottomSheetDialog
    lateinit var viewDataBinding: VB
    lateinit var viewModel: V
    lateinit var gpsListener: () -> Unit

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var intentProviderFactory: IntentProviderFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var moshi: Gson

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        init()
        setupFocusOutside(findViewById(android.R.id.content))
        manageSystemNavigationButtons()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    @LayoutRes
    abstract fun getLayout(): Int
    abstract fun init()

    abstract fun onViewModelCreated(viewModel: V)

    abstract fun onViewBound(viewDataBinding: VB)

    override fun onClick(p0: View?) {

    }

    fun <T : BaseViewModel<N>> getViewModel(viewModel: Class<T>): T {
        return ViewModelProvider(this, viewModelFactory).get(viewModel)
    }

    override fun onBackPress() {
        onBackPressedDispatcher.onBackPressed()
//        onBackPressed()
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

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onError(t: Throwable, showAction: Boolean, function: () -> Unit) {
        if (t is HttpException) {
            when (val code = t.code()) {
                401 -> {
                    showError(httpErrorMap[code] ?: return, showAction = false)
                    Handler().postDelayed({

                        onSessionExpired()
                    }, 2000)
                }
                else -> showError(httpErrorMap[code] ?: return, showAction = true) {
                    function()
                }
            }

        }
//        else {
//            showError(t.message ?: return, showAction = showAction) {
//                function()
//            }
//        }

        else {
            val error = if (t.toString().contains("java.net.UnknownHostException") ?: return)
                Throwable("Server not responding")
            else
                t
            showError(error.message ?: return, showAction = showAction) {
                function()
            }
        }
    }

    override fun onSuccess(message: String, showAction: Boolean, function: () -> Unit) {
        showSuccess(message, showAction, function)
    }

    fun onSessionExpired() {
        //Call activity on Session Expire
        startActivity(
            intentProviderFactory.create(
                SignUpOptionsActivity::class.java,
                null,
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
        viewModel.clearFlags()
        finish()
    }

    fun bind(viewModelClass: Class<V>) {
        viewModel = getViewModel(viewModelClass)
        onViewModelCreated(viewModel)
        viewDataBinding = DataBindingUtil.setContentView(this, getLayout())
        viewDataBinding.setVariable(BR.viewModel, viewModel)
        viewDataBinding.executePendingBindings()
        viewModel.setData(intent.extras)
        onViewBound(viewDataBinding)
        createGeneralNotificationChannel()
    }

    override fun onResume() {
        super.onResume()
        //    hideSystemUI()
//        unableDisableScreenTouch(viewModel.isLoading.get())
    }

    private fun manageSystemNavigationButtons() {
        hideNavigationButtons()
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                hideNavigationButtons()
            }
        }
    }

    fun hideNavigationButtons() {
//        window.decorView.apply {
//            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        }
        hideSystemUI()
    }


    fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    //Removes the focus from text fields when the user click on the screen
    private fun setupFocusOutside(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupFocusOutside(innerView)
            }
        }
    }

    //Hides the Keyboard when the User clicks on the screen
    fun hideKeyboard() {

        val currentView = this.currentFocus
        if (currentView != null) {
            currentView.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentView.windowToken, 0)
        }
    }

    private fun createGeneralNotificationChannel() {
        val sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.general_channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                getString(R.string.general_notification_channel_id),
                name,
                importance
            ).apply {
                description = descriptionText
            }
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            // Register the channel with the system

            channel.setSound(sound, attributes)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createFile(context: Context, srcUri: Uri, dstFile: File) {
//        try {
//            val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
//            val outputStream = FileOutputStream(dstFile)
//            IOUtils.copy(inputStream, outputStream)
//            inputStream.close()
//            outputStream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    /**
     * @author Muheeb Mehraj
     * @param Required Permission details, Permission Description
     * @description Used to request multiple permissions at once which are handled by Easy Permssion
     * library.
     */
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
        afterTask: () -> Unit,
    ) {
        if (EasyPermissions.hasPermissions(this, *rcList)) {
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
        grantResults: IntArray,
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
        Toast.makeText(this, "Permission exists", Toast.LENGTH_LONG).show()
    }

    fun setSpannableTextWithColor(
        view: TextView,
        resourceId: Int,
        email: String,
        userColor: Int,
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
                        R.color.black
                    );
                    ds.typeface = Typeface.DEFAULT_BOLD
                }
            }, emailIndex, emailIndex + email.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

    }

    override fun showProfileCompletionAlert(message: String) {
        CompleteProfileDialog.Builder()
            .setDescription(message)
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
            .show(this)
    }


    private fun getMaskLength(email: String): Int {
        val length = email.split("@")[0].length
        return length * 2 / 3
    }


    //higher order function recivied as params
    fun checkLocationSettings(listener: () -> Unit) {
        gpsListener = listener
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        result.addOnCompleteListener {
            try {
                val response = it.getResult(ApiException::class.java)
                listener()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
//                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
//                            resolvable.startResolutionForResult(
//                                this,
//                                REQUEST_CHECK_SETTINGS
//                            )

                            if (exception is ResolvableApiException) {
                                try {
                                    val intentSenderRequest = IntentSenderRequest
                                        .Builder(exception.resolution).build()
                                    resolutionForResult.launch(intentSenderRequest)
                                } catch (throwable: Throwable) {
                                    // Ignore the error.
                                }
                            }
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. And cannot be fixed
                        Toast.makeText(this, "Location Settings cannot change", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    //Received GPS operation
    private val resolutionForResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK)
            gpsListener()
    }

//    fun unableDisableScreenTouch(isScreenDisable: Boolean) {
//        if (isScreenDisable) {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        }
//    }
}
