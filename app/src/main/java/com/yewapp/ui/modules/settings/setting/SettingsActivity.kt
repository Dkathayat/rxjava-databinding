package com.yewapp.ui.modules.settings.setting

import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.databinding.ActivitySettingsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.about.AboutActivity
import com.yewapp.ui.modules.about.CmsExtras
import com.yewapp.ui.modules.chat.ChatActivity
import com.yewapp.ui.modules.chat.extras.ChatActivityExtras
import com.yewapp.ui.modules.cms.CmsActivity
import com.yewapp.ui.modules.faqs.FaqsActivity
import com.yewapp.ui.modules.feedback.ShareFeedbackActivity
import com.yewapp.ui.modules.managedevices.ManageDevicesActivity
import com.yewapp.ui.modules.managefeeds.ManageFeedsActivity
import com.yewapp.ui.modules.managenotification.ManageNotificationActivity
import com.yewapp.ui.modules.manageshortvideo.ManageShortVideoActivity
import com.yewapp.ui.modules.manageuser.ManageUserActivity
import com.yewapp.ui.modules.refer.ReferralActivity
import com.yewapp.ui.modules.settings.contactus.ContactUsActivity
import com.yewapp.ui.modules.subscription.SubscriptionActivity

class SettingsActivity :
    BaseActivity<SettingsNavigator, SettingsViewModel, ActivitySettingsBinding>(),
    SettingsNavigator {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    override fun getLayout(): Int = R.layout.activity_settings

    override fun init() {
        bind(SettingsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SettingsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivitySettingsBinding) {
    }

    override fun navigateToChatWithParent() {
        /**
         * @author: Narbir Singh
         * @description:
         * Case:1
         *         We Associate parent data as Associate data object for common chat process
         */
        val extrasAssociateParentDetails =
            ChatActivityExtras.associateDetailsExtras {
                associateDetails = Associate(
                    ",",
                    "",
                    "",
                    "",
                    "Narbir Parent",
                    "https://sites.google.com/site/narbirsinghpachgaon/_/rsrc/1620644354594/home/img.jpg?height=200&width=151",
                    "Parent",
                    "1",
                    77
                )
            }
        startActivity(
            intentProviderFactory.create(
                ChatActivity::class.java, extrasAssociateParentDetails, 0
            )
        )
    }

    override fun navigateToContactUs() {
        startActivity(
            intentProviderFactory.create(
                ContactUsActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToAbout() {
        startActivity(
            intentProviderFactory.create(
                AboutActivity::class.java,
                CmsExtras.cmsExtras {
                    link = "about"
                },
                0
            )
        )
    }

    override fun navigateToShareFeedback() {
        startActivity(
            intentProviderFactory.create(
                ShareFeedbackActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToFaqs() {
        startActivity(
            intentProviderFactory.create(
                FaqsActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToRefer() {
        startActivity(
            intentProviderFactory.create(
                ReferralActivity::class.java,
                null,
                0
            )
        )
    }

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

    override fun navigateToManageNotification() {
        startActivity(
            intentProviderFactory.create(
                ManageNotificationActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToManageUser() {
        startActivity(
            intentProviderFactory.create(
                ManageUserActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToManageFeeds() {
        startActivity(
            intentProviderFactory.create(
                ManageFeedsActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToManageShort() {
        startActivity(
            intentProviderFactory.create(
                ManageShortVideoActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToSubscription() {
        startActivity(
            intentProviderFactory.create(
                SubscriptionActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToManageDevices() {
        startActivity(
            intentProviderFactory.create(
                ManageDevicesActivity::class.java,
                null,
                0
            )
        )
    }


    override fun onLogoutSuccess() {
        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("FirebaseLog",it.toString())
            } else if(it.isCanceled){
                Log.d("FirebaseLog",it.toString())
            }

        }.addOnFailureListener {
            Log.d("FirebaseLog",it.toString())
        }
        faceBookLogOut()
        viewModel.clearFlags()
        onSessionExpired()
    }

    private fun faceBookLogOut() {
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE,
                {
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()
                }
            ).executeAsync()
        }
    }

}