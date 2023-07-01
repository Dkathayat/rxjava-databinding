package com.yewapp.di.app

import com.yewapp.di.addassociate.AddAssociateModule
import com.yewapp.di.addmodelequipmentdetails.AddModelEquipmentModule
import com.yewapp.di.chat.ChatModule
import com.yewapp.di.dashboard.DashboardModule
import com.yewapp.di.follower.FollowerModule
import com.yewapp.di.forgotpassword.ForgotPasswordModule
import com.yewapp.di.managedevices.ManageDevicesModule
import com.yewapp.di.managefeeds.ManageFeedsModule
import com.yewapp.di.managenotification.ManageNotificationModule
import com.yewapp.di.manageshortvideo.ManageShortModule
import com.yewapp.di.manageuser.ManageUserModule
import com.yewapp.di.onBoarding.OnBoardModule
import com.yewapp.di.profile.ProfileModule
import com.yewapp.di.routelisting.RouteListingModule
import com.yewapp.di.sharefeedback.ShareFeedbackModule
import com.yewapp.di.subscription.SubscriptionModule
import com.yewapp.di.videofeedcomment.VideoFeedCommentModule
import com.yewapp.di.videofeedfollower.VideoFeedFollowerModule
import com.yewapp.ui.modules.about.AboutActivity
import com.yewapp.ui.modules.addassociatemember.AddMembersActivity
import com.yewapp.ui.modules.addassociatememberdetails.AddMembersDetailsActivity
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionActivity
import com.yewapp.ui.modules.addchallenge.addchallengedetails.ChallengeLocationDetailActivity
import com.yewapp.ui.modules.addchallenge.addchallengeselectrangedate.SelectRangeDateActivity
import com.yewapp.ui.modules.addchallenge.addchallengesportstype.AddChallengeActivity
import com.yewapp.ui.modules.addchallenge.additionalform.AdditionalFormActivity
import com.yewapp.ui.modules.addchallenge.challengeadditionaldetail.ChallengeAdditionalDetailActivity
import com.yewapp.ui.modules.addchallenge.challengebanner.ChallengeBannerActivity
import com.yewapp.ui.modules.addchallenge.challengepreview.ChallengePreviewActivity
import com.yewapp.ui.modules.addchallenge.challengeroutes.RouteListingActivity
import com.yewapp.ui.modules.addchallenge.invitemember.InviteMemberActivity
import com.yewapp.ui.modules.addchallenge.minimumgoal.MinGoalRequirementActivity
import com.yewapp.ui.modules.addchallenge.selectorcreateroute.SelectORCreateRouteActivity
import com.yewapp.ui.modules.addmember.AddMemberFollowActivity
import com.yewapp.ui.modules.addmodelandequipments.AddModelEquipmentDetailsActivity
import com.yewapp.ui.modules.addspectator.AddSpectatorActivity
import com.yewapp.ui.modules.challengeDetails.ChallengeDescriptionDetailActivity
import com.yewapp.ui.modules.challengeDetails.ChallengeDetailActivity
import com.yewapp.ui.modules.chat.ChatActivity
import com.yewapp.ui.modules.cms.CmsActivity
import com.yewapp.ui.modules.comments.CommentsActivity
import com.yewapp.ui.modules.createroute.CreateRouteActivity
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.ui.modules.dashboard.fragment.feeds.FeedsLikedActivity
import com.yewapp.ui.modules.editProfile.EditProfileActivity
import com.yewapp.ui.modules.editProfile.SportTypeActivity
import com.yewapp.ui.modules.emailchange.EmailChangeActivity
import com.yewapp.ui.modules.emailphonechangeverifyotp.EmailPhoneChangeVerifyCodeActivity
import com.yewapp.ui.modules.faqs.FaqsActivity
import com.yewapp.ui.modules.feed.CreateFeedActivity
import com.yewapp.ui.modules.feedback.ShareFeedbackActivity
import com.yewapp.ui.modules.follower.FollowerActivity
import com.yewapp.ui.modules.forgotPassword.ChangePasswordSuccessActivity
import com.yewapp.ui.modules.forgotPassword.ForgotPasswordActivity
import com.yewapp.ui.modules.giveawayDetails.GiveAwayDetailsActivity
import com.yewapp.ui.modules.login.LoginActivity
import com.yewapp.ui.modules.managedevices.ManageDevicesActivity
import com.yewapp.ui.modules.managedevices.extra.DeviceConnectInfoActivity
import com.yewapp.ui.modules.managedevices.garmin.MainManageDevicesActivity
import com.yewapp.ui.modules.managefeeds.ManageFeedsActivity
import com.yewapp.ui.modules.managenotification.ManageNotificationActivity
import com.yewapp.ui.modules.manageshortvideo.ManageShortVideoActivity
import com.yewapp.ui.modules.manageuser.ManageUserActivity
import com.yewapp.ui.modules.migrateassociate.MigrateAssociateActivity
import com.yewapp.ui.modules.migrateassociatesucess.MigrateAssociateSuccessActivity
import com.yewapp.ui.modules.onboarding.OnBoardingActivity
import com.yewapp.ui.modules.player.PlayerVideoActivity
import com.yewapp.ui.modules.profile.fragment.mypoints.filteractivity.MyPointsFilterActivity
import com.yewapp.ui.modules.profilesubsportstype.ProfileSubSportsTypeActivity
import com.yewapp.ui.modules.refer.ReferralActivity
import com.yewapp.ui.modules.report.SendReportActivity
import com.yewapp.ui.modules.routedetail.RouteDetailActivity
import com.yewapp.ui.modules.searchlocation.SearchLocationActivity
import com.yewapp.ui.modules.settings.contactus.ContactUsActivity
import com.yewapp.ui.modules.settings.setting.SettingsActivity
import com.yewapp.ui.modules.signup.SignUpActivity
import com.yewapp.ui.modules.signup.SignUpOptionsActivity
import com.yewapp.ui.modules.signup.SignUpSuccessActivity
import com.yewapp.ui.modules.splash.SplashActivity
import com.yewapp.ui.modules.subscription.SubscriptionActivity
import com.yewapp.ui.modules.updateparticipants.UpdateParticipantsActivity
import com.yewapp.ui.modules.verifyCode.VerifyCodeActivity
import com.yewapp.ui.modules.videofeeds.VideoFeedProfileActivity
import com.yewapp.ui.modules.videofeeds.VideoFeedSearchActivity
import com.yewapp.ui.modules.videofeeds.VideoFeedUserFollowerActivity
import com.yewapp.ui.modules.videofeeds.VideoFeedsActivity
import com.yewapp.ui.modules.viewMedia.ViewImageVideoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector
    abstract fun provideSplashActivity(): SplashActivity


    @ContributesAndroidInjector(modules = [OnBoardModule::class])
    abstract fun provideOnBoardingActivity(): OnBoardingActivity

    @ContributesAndroidInjector
    abstract fun provideSignUpOptionsActivity(): SignUpOptionsActivity

    @ContributesAndroidInjector
    abstract fun provideLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun provideSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = [ShareFeedbackModule::class])
    abstract fun provideShareFeedbackActivity(): ShareFeedbackActivity

    @ContributesAndroidInjector(modules = [ForgotPasswordModule::class])
    abstract fun provideForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector
    abstract fun provideVerifyCodeActivity(): VerifyCodeActivity

    @ContributesAndroidInjector
    abstract fun provideSignUpSuccessActivity(): SignUpSuccessActivity

    @ContributesAndroidInjector
    abstract fun provideChangePasswordSuccessActivity(): ChangePasswordSuccessActivity

    @ContributesAndroidInjector(modules = [DashboardModule::class])
    abstract fun provideDashboardActivity(): DashboardActivity

    @ContributesAndroidInjector
    abstract fun provideCreateFeedActivity(): CreateFeedActivity

    @ContributesAndroidInjector(modules = [FollowerModule::class])
    abstract fun provideFollowerActivity(): FollowerActivity

    @ContributesAndroidInjector
    abstract fun provideSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun provideMyPointsFilterActivity(): MyPointsFilterActivity

    @ContributesAndroidInjector
    abstract fun provideDeviceConnectInfoActivity(): DeviceConnectInfoActivity

    @ContributesAndroidInjector
    abstract fun provideContactUsActivity(): ContactUsActivity

    @ContributesAndroidInjector
    abstract fun provideAboutActivity(): AboutActivity

    @ContributesAndroidInjector
    abstract fun provideChallengeDetailActivity(): ChallengeDetailActivity

    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun provideEditProfileActivity(): EditProfileActivity

    @ContributesAndroidInjector
    abstract fun provideFaqsActivity(): FaqsActivity

    @ContributesAndroidInjector(modules = [DashboardModule::class])
    abstract fun provideReferralActivity(): ReferralActivity

    @ContributesAndroidInjector
    abstract fun provideSendReportActivity(): SendReportActivity

    @ContributesAndroidInjector
    abstract fun provideCommentsActivity(): CommentsActivity

    @ContributesAndroidInjector
    abstract fun provideViewImageVideoActivity(): ViewImageVideoActivity

    @ContributesAndroidInjector
    abstract fun provideAddChallengeActivity(): AddChallengeActivity

    @ContributesAndroidInjector
    abstract fun provideFeedsLikedActivity(): FeedsLikedActivity

    @ContributesAndroidInjector
    abstract fun provideGiveAwayDetailsActivity(): GiveAwayDetailsActivity

    @ContributesAndroidInjector
    abstract fun provideChallengeDescriptionDetailActivity(): ChallengeDescriptionDetailActivity

    @ContributesAndroidInjector
    abstract fun provideAddMembersActivity(): AddMembersActivity

    @ContributesAndroidInjector
    abstract fun provideSelectRangeDateActivity(): SelectRangeDateActivity

    @ContributesAndroidInjector
    abstract fun provideChallengeLocationDetailActivity(): ChallengeLocationDetailActivity

    @ContributesAndroidInjector
    abstract fun provideCmsActivity(): CmsActivity

    @ContributesAndroidInjector
    abstract fun provideSportTypeActivity(): SportTypeActivity

//    @ContributesAndroidInjector
//    abstract fun provideMapActivity(): SelectRouteMapActivity

    @ContributesAndroidInjector
    abstract fun provideOptimizeDirectionMapActivity(): SelectORCreateRouteActivity

    @ContributesAndroidInjector(modules = [RouteListingModule::class])
    abstract fun provideRouteListingActivity(): RouteListingActivity

    @ContributesAndroidInjector
    abstract fun provideRouteDetailActivity(): RouteDetailActivity

    @ContributesAndroidInjector
    abstract fun provideChallengeAdditionalDetailActivity(): ChallengeAdditionalDetailActivity

    @ContributesAndroidInjector
    abstract fun provideSearchLocationActivity(): SearchLocationActivity

    @ContributesAndroidInjector
    abstract fun provideMinGoalRequirementActivity(): MinGoalRequirementActivity

    @ContributesAndroidInjector
    abstract fun provideAdditionalFormActivity(): AdditionalFormActivity

    @ContributesAndroidInjector
    abstract fun provideChallengeBannerActivity(): ChallengeBannerActivity

    @ContributesAndroidInjector
    abstract fun provideInviteMemberActivity(): InviteMemberActivity

    @ContributesAndroidInjector
    abstract fun provideChallengePreviewActivity(): ChallengePreviewActivity

    @ContributesAndroidInjector(modules = [VideoFeedCommentModule::class])
    abstract fun provideVideoFeedsActivity(): VideoFeedsActivity

    @ContributesAndroidInjector
    abstract fun provideVideoFeedProfileActivity(): VideoFeedProfileActivity

    @ContributesAndroidInjector
    abstract fun provideVideoFeedSearchActivity(): VideoFeedSearchActivity

    @ContributesAndroidInjector(modules = [ManageUserModule::class])
    abstract fun provideManageUserActivity(): ManageUserActivity

    @ContributesAndroidInjector(modules = [ManageNotificationModule::class])
    abstract fun provideManageNotificationActivity(): ManageNotificationActivity

    @ContributesAndroidInjector(modules = [ManageFeedsModule::class])
    abstract fun provideManageFeedsActivity(): ManageFeedsActivity

    @ContributesAndroidInjector(modules = [ManageDevicesModule::class])
    abstract fun provideMangeDevicesActivity(): ManageDevicesActivity

    @ContributesAndroidInjector(modules = [ManageShortModule::class])
    abstract fun provideManageShortActivity(): ManageShortVideoActivity


    @ContributesAndroidInjector(modules = [SubscriptionModule::class])
    abstract fun provideSubscriptionActivity(): SubscriptionActivity

    @ContributesAndroidInjector()
    abstract fun providePlayerVideoActivity(): PlayerVideoActivity

    @ContributesAndroidInjector()
    abstract fun provideGarminActivity(): MainManageDevicesActivity

    @ContributesAndroidInjector()
    abstract fun provideEmailChangeActivity(): EmailChangeActivity

    @ContributesAndroidInjector()
    abstract fun provideEmailPhoneChangeVerifyCodeActivity(): EmailPhoneChangeVerifyCodeActivity

    @ContributesAndroidInjector()
    abstract fun provideAddMemberFollowActivity(): AddMemberFollowActivity


    @ContributesAndroidInjector(modules = [VideoFeedFollowerModule::class])
    abstract fun provideVideoFeedUserFollowerActivity(): VideoFeedUserFollowerActivity

    @ContributesAndroidInjector
    abstract fun provideCreateRouteActivity(): CreateRouteActivity

    @ContributesAndroidInjector(modules = [AddAssociateModule::class])
    abstract fun provideAddMembersDetailsActivity(): AddMembersDetailsActivity

    @ContributesAndroidInjector
    abstract fun provideAddAssociatePermissionActivity(): AddAssociatePermissionActivity

    @ContributesAndroidInjector
    abstract fun provideMigrateAssociateActivity(): MigrateAssociateActivity

    @ContributesAndroidInjector
    abstract fun provideMigrateAssociateSuccessActivity(): MigrateAssociateSuccessActivity

    @ContributesAndroidInjector(modules = [ChatModule::class])
    abstract fun provideChatActivity(): ChatActivity

    @ContributesAndroidInjector
    abstract fun provideAddSpectatorActivity(): AddSpectatorActivity

    @ContributesAndroidInjector
    abstract fun provideProfileSubSportsTypeActivity(): ProfileSubSportsTypeActivity

    @ContributesAndroidInjector(modules = [AddModelEquipmentModule::class])
    abstract fun provideAddModelEquipmentDetailsActivity(): AddModelEquipmentDetailsActivity

    @ContributesAndroidInjector
    abstract fun provideUpdateParticipantsActivity(): UpdateParticipantsActivity
}
