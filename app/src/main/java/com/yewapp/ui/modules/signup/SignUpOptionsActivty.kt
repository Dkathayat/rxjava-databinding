package com.yewapp.ui.modules.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Observable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableField
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yewapp.R
import com.yewapp.data.local.PreferenceKeys
import com.yewapp.databinding.ActivitySignUpOptionsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.about.CmsExtras
import com.yewapp.ui.modules.cms.CmsActivity
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.ui.modules.login.LoginActivity
import com.yewapp.ui.modules.signup.navigator.SignUpOptionsNavigator
import com.yewapp.ui.modules.signup.vm.SignUpOptionsViewModel
import com.yewapp.utils.PREF_NAME
import org.json.JSONException
import org.json.JSONObject


private lateinit var callbackManager: CallbackManager
private lateinit var mGoogleSignInClient: GoogleSignInClient
private lateinit var firebaseAuth: FirebaseAuth


class SignUpOptionsActivity :
    BaseActivity<SignUpOptionsNavigator, SignUpOptionsViewModel, ActivitySignUpOptionsBinding>(),
    SignUpOptionsNavigator {
    override fun getLayout(): Int = R.layout.activity_sign_up_options

    override fun init() {
        bind(SignUpOptionsViewModel::class.java)

    }

    override fun onViewModelCreated(viewModel: SignUpOptionsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySignUpOptionsBinding) {
        callbackManager = CallbackManager.Factory.create()
        //faceBookLoginStatus()

    }

    override fun navigateToLogin() {
        startActivity(
            intentProviderFactory.create(
                LoginActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToSignUp() {
        startActivity(
            intentProviderFactory.create(
                SignUpActivity::class.java,
                null,
                0
            )
        )
    }

    override fun onSignUpSocialSuccess() {
//        val allreadySigned = viewModel.perviousSignUpValidation
//        if (allreadySigned.isNullOrEmpty()) {
        startActivity(
            intentProviderFactory.create(
                SignUpSuccessActivity::class.java,
                null,
                0
            )
        )
        finish()
//        } else {
//            startActivity(
//                intentProviderFactory.create(
//                    DashboardActivity::class.java,
//                    null,
//                    0
//                )
//            )
//            finish()
//        }
    }

    override fun navigateToFacebookSignUp() {
        facebookSignUpImp()
    }

    override fun navigateToGoogleSignUp() {
        FirebaseAuth.getInstance().signOut()
        FirebaseApp.initializeApp(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
        signInGoogle()
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
         resultLauncher.launch(signInIntent)
//        startActivityForResult(signInIntent,0)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode==0){
//            val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleResult(task)
//        }
//    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {

        viewDataBinding.loadingPanel.visibility = View.VISIBLE
        viewDataBinding.loadingPanel.setOnClickListener {
            Snackbar.make(viewDataBinding.view, "Please wait...", Snackbar.LENGTH_SHORT).show()
        }
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            viewDataBinding.loadingPanel.visibility = View.GONE
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                try {
                    val token = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                        PreferenceKeys.FIREBASE_TOKEN, "empty"
                    )
                    if (firebaseAuth.currentUser != null) {
                        viewModel.socialSignUp(
                            token.toString(),
                            "1",
                            firebaseAuth.currentUser!!.email.toString(),
                            "google",
                            firebaseAuth.uid.toString()
                        )
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("TAG", e.stackTraceToString())
                }
            }
        }
    }

    private fun getFacebookEmail(): String {
        var email = ""
        GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { _, response ->
            val json = response!!.getJSONObject()
            try {
                if (json != null) {
                    email = json.getString("email")

                }
            } catch (_: JSONException) {
            }
        }
        return email.trim()
    }


    private fun facebookSignUpImp() {
        val loginManager = LoginManager.getInstance()
        viewDataBinding.loadingPanel.visibility = View.VISIBLE
        viewDataBinding.loadingPanel.setOnClickListener {
            Toast.makeText(this, "Please Wait.....", Toast.LENGTH_SHORT).show()
        }
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onCancel() {
                viewDataBinding.loadingPanel.visibility = View.GONE
            }

            override fun onError(error: FacebookException) {
                viewDataBinding.loadingPanel.visibility = View.GONE
                Toast.makeText(
                    this@SignUpOptionsActivity,
                    "Something went wrong please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onSuccess(result: LoginResult) {
                val email = getFacebookEmail()
                val token = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                    PreferenceKeys.FIREBASE_TOKEN, "empty"
                )
                viewModel.socialSignUp(
                    token.toString(),
                    "1",
                    email,
                    "facebook",
                    result.accessToken.userId
                ).toString()
            }

        })
        loginManager.logInWithReadPermissions(this, listOf("email"))
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data)
//    }


    override fun navigateToTermsAndCondition() {
        startActivity(
            intentProviderFactory.create(
                CmsActivity::class.java,
                CmsExtras.cmsExtras {
                    link = "terms-of-use"
                },
                0
            )
        )
    }

    private fun faceBookLoginStatus() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if ((accessToken != null) && !accessToken.isExpired) {
            startActivity(Intent(this@SignUpOptionsActivity, SignUpSuccessActivity::class.java))
            finish()
        }
    }

//    override fun onStart() {
//        super.onStart()
//        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//            startActivity(
//                Intent(intentProviderFactory.create(
//                    DashboardActivity::class.java,
//                    null,
//                    0
//                )
//                )
//            )
//            finish()
//        }
//    }

    override fun navigateToPrivacyPolicy() {
        startActivity(
            intentProviderFactory.create(
                CmsActivity::class.java,
                CmsExtras.cmsExtras {
                    link = "privacy-policy"
                },
                0
            )
        )
    }
}