package com.yewapp.data.network

import com.yewapp.data.local.PreferenceKeys
import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.network.api.FollowUpdateResponse
import com.yewapp.data.network.api.FollowersUpdateResponse
import com.yewapp.data.network.api.ManageUserListResponse
import com.yewapp.data.network.api.about.CmsResponse
import com.yewapp.data.network.api.addmodelequipment.AddModelEquipmentRequest
import com.yewapp.data.network.api.addmodelequipment.AddModelEquipmentResponse
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentRequest
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentResponse
import com.yewapp.data.network.api.associate.*
import com.yewapp.data.network.api.challenges.*
import com.yewapp.data.network.api.comment.CommentRequest
import com.yewapp.data.network.api.comment.CommentResponse
import com.yewapp.data.network.api.comment.GetFeedReportedComments
import com.yewapp.data.network.api.contact.ContactRequest
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.data.network.api.emailchange.*
import com.yewapp.data.network.api.faqs.FaqData
import com.yewapp.data.network.api.feed.*
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.follower.FollowerListingResponse
import com.yewapp.data.network.api.follower.FollowingListingResponse
import com.yewapp.data.network.api.forgotpassword.ChangePasswordRequest
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordRequest
import com.yewapp.data.network.api.forgotpassword.ForgotPasswordResponse
import com.yewapp.data.network.api.invitemember.InviteMemberListResponse
import com.yewapp.data.network.api.login.LoginRequest
import com.yewapp.data.network.api.mypoint.MyPointResponse
import com.yewapp.data.network.api.mypoint.MyPointsHistoryResponse
import com.yewapp.data.network.api.mypoint.UserPointSummaryResponse
import com.yewapp.data.network.api.notificationsetting.NotificationSettingRequest
import com.yewapp.data.network.api.profile.*
import com.yewapp.data.network.api.profile.State
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.data.network.api.refer.ReferResponse
import com.yewapp.data.network.api.refer.ReferRewardHistoryResponse
import com.yewapp.data.network.api.report.*
import com.yewapp.data.network.api.routes.CreateRouteRequest
import com.yewapp.data.network.api.routes.CreateRouteResponse
import com.yewapp.data.network.api.routes.RouteDetailResponse
import com.yewapp.data.network.api.routes.RouteListResponse
import com.yewapp.data.network.api.signup.*
import com.yewapp.data.network.api.spectator.AddSpectatorRequest
import com.yewapp.data.network.api.spectator.DeleteSpectatorRequest
import com.yewapp.data.network.api.spectator.SpectatorMemberResponse
import com.yewapp.data.network.api.spectator.YewMemberResponse
import com.yewapp.data.network.api.sports.DeleteSportsRequest
import com.yewapp.data.network.api.sports.Model
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.data.network.api.sports.ProfileSportsResponse
import com.yewapp.data.network.api.subscription.SubscriptionHistory
import com.yewapp.data.network.api.subscription.SubscriptionPlans
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.data.network.api.verify.VerifyRequest
import com.yewapp.data.network.api.video.*
import com.yewapp.di.addassociate.ValidateEmailRequest
import com.yewapp.utils.resource.ResourceProvider
import io.reactivex.Observable
import javax.inject.Inject

class DataManagerImpl @Inject constructor(
    var apiHelper: ApiHelper,
    var preferencesHelper: PreferencesHelper,
    var resourceProviders: ResourceProvider,
) : DataManager {

    override fun signup(request: SignUpRequest): Observable<SignUpResponse> {
        return apiHelper.signUp(request)
    }

    override fun signUpSocial(request: SignUpRequestSocial): Observable<SignUpResponse> {
        return apiHelper.signUpSocial(request)
    }

    override fun verifyCode(request: VerifyRequest): Observable<SignUpResponse> {
        return apiHelper.verifyCode(request)
    }

    override fun forgotPassword(request: ForgotPasswordRequest): Observable<ForgotPasswordResponse> {
        return apiHelper.forgotPassword(request)
    }

    override fun changePassword(request: ChangePasswordRequest): Observable<String> {
        return apiHelper.changePassword(request)
    }

    override fun login(request: LoginRequest): Observable<SignUpResponse> {
        return apiHelper.login(request)
    }

    override fun contactUs(request: ContactRequest): Observable<String> {
        return apiHelper.contactUs(request)
    }

    override fun comment(request: CommentRequest): Observable<CommentListResponse> {
        return apiHelper.comment(request)
    }

    override fun logout(): Observable<String> {
        return apiHelper.logout()
    }

    override fun getFollowerList(): Observable<FollowerListingResponse> {
        return apiHelper.getFollowerList()
    }

    override fun getFollowingList(): Observable<FollowingListingResponse> {
        return apiHelper.getFollowingList()
    }

    override fun getProfile(): Observable<SignUpResponse> {
        return apiHelper.getProfile()
    }

    override fun faqs(): Observable<List<FaqData>> {
        return apiHelper.faqs()
    }

    override fun referFriendList(): Observable<List<ReferResponse>> {
        return apiHelper.referFriendList()
    }

    override fun referRewardHistory(page: Int): Observable<ReferRewardHistoryResponse> {
        return apiHelper.referRewardHistory(page)
    }

    override fun postActivityLike(activtyId: String, unicode: String): Observable<String> {
        return apiHelper.postActivityLike(activtyId,unicode)
    }

    override fun commentList(feedId: Int): Observable<CommentResponse> {
        return apiHelper.commentList(feedId)
    }

    override fun feedsLikedList(feedId: Int): Observable<FeedLikeResponse> {
        return apiHelper.feedsLikedList(feedId)
    }

    override fun reportReasonList(): Observable<List<ReportReason>> {
        return apiHelper.reportReasonList()
    }

    override fun sendReport(request: ReportPost): Observable<String> {
        return apiHelper.sendReport(request)
    }

    override fun getFeedReports(): Observable<GetReportedPost> {
        return apiHelper.getPostReported()
    }

    override fun getReportedShortVideos(): Observable<GetShortReportedVideo> {
        return apiHelper.getReportedShortVideos()
    }

    override fun sendReportForComment(request: CommentReportPost): Observable<String> {
        return apiHelper.sendReportForComment(request)
    }

    override fun deleteReportedComment(request: Int): Observable<String> {
        return apiHelper.deleteReportedComments(request)
    }

    override fun deleteReportedFeed(request: Int): Observable<String> {
        return apiHelper.deleteReportedFeed(request)
    }

    override fun getReportedFeedComments(): Observable<GetFeedReportedComments> {
        return apiHelper.getReportedFeedComment()
    }

    override fun getCmsPages(link: String): Observable<CmsResponse> {
        return apiHelper.getCmsPage(link)
    }

    override fun sendRefer(request: MutableList<PhoneContacts>): Observable<String> {
        return apiHelper.sendRefer(request)
    }

    override fun setNotificationSetting(notificationSettingRequest: NotificationSettingRequest): Observable<String> {
        return apiHelper.setNotificationSetting(notificationSettingRequest)
    }

    override fun getNotificationSetting(): Observable<NotificationSettingRequest> {
        return apiHelper.getNotificationSetting()
    }

    override fun getBlockedManageUserList(): Observable<ManageUserListResponse> {
        return apiHelper.getBlockedManageUserList()
    }

    override fun getMutedManageUserList(): Observable<ManageUserListResponse> {
        return apiHelper.getMutedManageUserList()
    }

    override fun getReportedManageUserList(): Observable<ManageUserListResponse> {
        return apiHelper.getReportedManageUserList()
    }

    override fun getFavoriteManageUserList(): Observable<ManageUserListResponse> {
        return apiHelper.getFavoriteManageUserList()
    }

    override fun sendFeedReport(request: SendFeedReport): Observable<String> {
        return apiHelper.sendFeedReport(request)
    }

    override fun setUserOnBoarded(onBoard: Boolean) {
        preferencesHelper.saveBoolean(PreferenceKeys.ON_BOARDING_STATUS, true)
    }

    override fun getCountries(): Observable<CountryListResponse> {
        return apiHelper.getCountries()
    }

    override fun getStates(id: String): Observable<List<State>> {
        return apiHelper.getStates(id)
    }

    override fun getCities(id: String): Observable<List<City>> {
        return apiHelper.getCities(id)
    }

    override fun updateProfile(request: ProfileRequest): Observable<SignUpResponse> {
        return apiHelper.updateProfile(request)
    }

    override fun getSportTypes(): Observable<List<SportType>> {
        return apiHelper.getSportTypes()
    }

    override fun createFeed(request: CreateFeedRequest): Observable<String> {
        return apiHelper.createFeed(request)
    }

    override fun getFeed(
        search: String,
        userId: Int?,
        startDate: String,
        endDate: String,
        currentPage: Int,
    ): Observable<FeedResponse> {
        return apiHelper.getFeed(search, userId, startDate, endDate, currentPage)
    }

    override fun blockUser(request: BlockUserRequest): Observable<String> {
        return apiHelper.blockUser(request)
    }

    override fun sendFeedback(request: SendFeedback): Observable<String> {
        return apiHelper.sendFeedback(request)
    }

    override fun favouriteUser(request: BlockUserRequest): Observable<String> {
        return apiHelper.favouriteUser(request)
    }

    override fun muteUser(request: BlockUserRequest): Observable<String> {
        return apiHelper.muteUser(request)
    }

    override fun followUserFeed(request: BlockUserRequest): Observable<String> {
        return apiHelper.followUserFeed(request)
    }

    override fun followUser(request: BlockUserRequest): Observable<FollowUpdateResponse> {
        return apiHelper.followUser(request)
    }

    override fun followerFollowUser(request: BlockUserRequest): Observable<FollowersUpdateResponse> {
        return apiHelper.followerFollowUser(request)
    }

    override fun getActivityFilterData(request: ActivitiesFilterRequest): Observable<ActivitiesFilterResponseData> {
        return apiHelper.getFilterActivityData(request)
    }

    override fun likeFeed(request: LikeFeedRequest): Observable<Feed> {
        return apiHelper.likeFeed(request)
    }

    override fun likeVideFeed(request: LikeFeedRequest): Observable<VideoData> {
        return apiHelper.likeVideFeed(request)
    }

    override fun getSubscriptionHistory(): Observable<SubscriptionHistory> {
        return apiHelper.getSubscriptionHistory()
    }

    override fun getSubscriptionPlansDetails(): Observable<SubscriptionPlans> {
        return apiHelper.getSubscriptionPlansList()
    }

    override fun saveVideo(saveVideoRequest: SaveVideoRequest): Observable<Any> {
        return apiHelper.saveVideo(saveVideoRequest)
    }

    override fun getPublishVideoList(): Observable<VideoListResponse> {
        return apiHelper.getPublishVideoList()
    }

    override fun getUserPublishVideoList(
        search: String?, userId: Int
    ): Observable<VideoListResponse> {
        return apiHelper.getUserPublishVideoList(search, userId)
    }


    override fun getSportsList(): Observable<List<Sport>> {
        return apiHelper.getSportsList()
    }

    override fun getProfileSportsSubSportList(sportId: Int): Observable<List<ProfileSportsResponse>> {
        return apiHelper.getProfileSportsSubSportList(sportId)
    }

    override fun getProfileSportsList():Observable<List<ProfileSportsResponse>> {
        return apiHelper.getProfileSportsList()
    }

    override fun getSubSportsList(parentSportId: Int): Observable<List<Sport>> {
        return apiHelper.getSubSportsList(parentSportId)
    }

    override fun getRouteList(type: String): Observable<RouteListResponse> {
        return apiHelper.getRouteList(type)
    }

    override fun getInviteMemberList(): Observable<InviteMemberListResponse> {
        return apiHelper.getInviteMemberList()
    }

    override fun getSubSportsModelList(subSportId: Int): Observable<List<Model>> {
        return apiHelper.getSubSportsModelList(subSportId)
    }

    override fun getRouteDetail(id: Int): Observable<RouteDetailResponse> {
        return apiHelper.getRouteDetail(id)
    }

    override fun addToFavorite(id: Int): Observable<String> {
        return apiHelper.addToFavorite(id)
    }

    override fun getCommentList(feedId: Int): Observable<CommentListResponse> {
        return apiHelper.getCommentList(feedId)
    }

    override fun createChallenge(request: CreateChallengeRequest): Observable<CreateChallengeResponse> {
        return apiHelper.createChallenge(request)
    }

    override fun createRoute(request: CreateRouteRequest): Observable<CreateRouteResponse> {
        return apiHelper.createRoute(request)
    }


    override fun getUserOnBoarded(): Boolean {
        return preferencesHelper.getBoolean(PreferenceKeys.ON_BOARDING_STATUS, false)
    }

    override fun setLoginStatus(loginStatus: Boolean) {
        preferencesHelper.saveBoolean(PreferenceKeys.LOGIN_STATUS, loginStatus)
    }

    override fun getLoginStatus(): Boolean {
        return preferencesHelper.getBoolean(PreferenceKeys.LOGIN_STATUS, false)
    }

    override fun getPreference(): PreferencesHelper {
        return preferencesHelper
    }

    override fun getResourceProvider(): ResourceProvider {
        return resourceProviders
    }

    override fun saveUser(response: Profile?) {
        preferencesHelper.saveObject(PreferenceKeys.USER_DATA, response)
    }

    override fun getUser(): Profile {
        return preferencesHelper.getObject(PreferenceKeys.USER_DATA, Profile::class.java)
    }

    override fun saveSubscription(subscriptionDetail: SubscriptionDetail?) {
        preferencesHelper.saveObject(PreferenceKeys.USER_SUBSCRIPTION, subscriptionDetail)
    }

    override fun getSubscription(): SubscriptionDetail {
        return preferencesHelper.getObject(
            PreferenceKeys.USER_SUBSCRIPTION, SubscriptionDetail::class.java
        )
    }

    override fun saveAccessToken(accessToken: String) {
        preferencesHelper.saveString(PreferenceKeys.API_TOKEN, accessToken)
    }

    override fun saveAwsCredential(awsCredential: AwsCredential) {
        preferencesHelper.saveObject(PreferenceKeys.AWS_CREDENTIAL, awsCredential)
    }

    override fun getAwsCredential(): AwsCredential {
        return preferencesHelper.getObject(PreferenceKeys.AWS_CREDENTIAL, AwsCredential::class.java)
    }

    override fun getAccessToken(): String {
        return preferencesHelper.getString(PreferenceKeys.API_TOKEN, "") ?: ""
    }

    override fun changeEmail(request: ChangeEmailRequest): Observable<String> {
        return apiHelper.changeEmail(request)
    }

    override fun changePhone(request: ChangePhoneRequest): Observable<String> {
        return apiHelper.changePhone(request)
    }

    override fun updateEmail(request: UpdateEmailRequest): Observable<String> {
        return apiHelper.updateEmail(request)
    }

    override fun updatePhone(request: UpdatePhoneRequest): Observable<String> {
        return apiHelper.updatePhone(request)
    }

    override fun likeComment(likeCommentRequest: LikeCommentRequest): Observable<CommentListResponse> {
        return apiHelper.likeComment(likeCommentRequest)
    }

    override fun getFavoritesUserList(): Observable<FavoritesUserResponse> {
        return apiHelper.getFavoritesUserList()
    }

    override fun getSuggestedUserList(): Observable<SuggestedUserResponse> {
        return apiHelper.getSuggestedUserList()
    }

    override fun getAllUserList(
        search: String, currentPage: Int
    ): Observable<ManageUserListResponse> {
        return apiHelper.getAllUserList(search, currentPage)
    }

    override fun videoFeedFollowerList(): Observable<FollowerListingResponse> {//userId: Int
        return apiHelper.videoFeedFollowerList()//userId
    }

    override fun videoFeedFollowingList(): Observable<FollowerListingResponse> { //userId: Int
        return apiHelper.videoFeedFollowingList()//userId
    }

    override fun getCommonChallengeList(type: String): Observable<AllChallengesResponse> {
        return apiHelper.getCommonChallengeList(type)
    }

    override fun getChallengeDetails(challengeId: Int): Observable<ChallengeDetailResponse> {
        return apiHelper.getChallengeDetails(challengeId)
    }

    override fun getMaxMemberCount(): Observable<MaxMemberResponse> {
        return apiHelper.getMaxMemberCount()
    }

    override fun addAssociateMember(request: AddAssociateRequest): Observable<AddAssociateResponse> {
        return apiHelper.addAssociateMember(request)
    }

    override fun updateAssociateMember(
        associateId: String, request: AddAssociateRequest
    ): Observable<String> {
        return apiHelper.updateAssociateMember(associateId, request)
    }

    override fun getAssociateMember(): Observable<AssociateMemberResponse> {
        return apiHelper.getAssociateMember()
    }

    override fun validateAssociateEmail(request: ValidateEmailRequest): Observable<String> {
        return apiHelper.validateAssociateEmail(request)
    }

    override fun getAssociatePermissions(associateId: String): Observable<AssociatePermissionResponse> {
        return apiHelper.getAssociatePermissions(associateId)
    }

    override fun getAssociateMemberDetails(associateId: String): Observable<AssociateMemberDetailsResponse> {
        return apiHelper.getAssociateMemberDetails(associateId)
    }

    override fun updateAssociatePermission(request: AssociatePermissionRequest): Observable<String> {
        return apiHelper.updateAssociatePermission(request)
    }

    override fun migrateAssociateAccount(request: MigrateAssociateAccountRequest): Observable<String> {
        return apiHelper.migrateAssociateAccount(request)
    }

    override fun removeAssociateAccount(request: RemoveAssociateAccountRequest): Observable<String> {
        return apiHelper.removeAssociateAccount(request)
    }

    override fun activateDeactivateAssociateAccount(request: ActiveDeActiveAssociateAccountRequest): Observable<String> {
        return apiHelper.activateDeactivateAssociateAccount(request)
    }

    override fun getAssociateSportsType(request: RemoveAssociateAccountRequest): Observable<AssociateSportsTypeResponse> {
        return apiHelper.getAssociateSportsType(request)
    }

    override fun saveAssociateSportsType(request: SaveAssociateSportsTypeRequest): Observable<String> {
        return apiHelper.saveAssociateSportsType(request)
    }

    override fun addSpectator(request: AddSpectatorRequest): Observable<String> {
        return apiHelper.addSpectator(request)
    }

    override fun getYewMembersForInvite(): Observable<YewMemberResponse> {
        return apiHelper.getYewMembersForInvite()
    }

    override fun getSpectatorMembers(): Observable<SpectatorMemberResponse> {
        return apiHelper.getSpectatorMembers()
    }

    override fun deleteSpectator(request: DeleteSpectatorRequest): Observable<String> {
        return apiHelper.deleteSpectator(request)
    }

    override fun getMyPoints(): Observable<MyPointResponse> {
        return apiHelper.getMyPoints()
    }

    override fun getUserPointSummary(
        page: Int,
        category: String,
        startDate: String,
        endDate: String,
        sportsId: String,
        subSportsId: List<String>
    ): Observable<UserPointSummaryResponse> {
        return apiHelper.getUserPointSummary(page, category, startDate, endDate,sportsId,subSportsId)
    }

    override fun getUserPointsHistory(
        page: Int,
        startDate: String,
        endDate: String,
        sportsId: List<String>,
        subSportId: List<String>
    ): Observable<MyPointsHistoryResponse> {
        return apiHelper.getUserHistorySummary(page, startDate, endDate, sportsId, subSportId)
    }

    override fun updateProfileSettings(request: ProfileSettingsRequest): Observable<String> {
        return apiHelper.updateProfileSettings(request)
    }

    override fun updateProfileImage(request: ProfileImageRequest): Observable<String> {
        return apiHelper.updateProfileImage(request)
    }

    override fun saveUserSportsEquipments(request: AddModelEquipmentRequest): Observable<List<AddModelEquipmentResponse>> {
        return apiHelper.saveUserSportsEquipments(request)
    }

    override fun deleteSportsEquipmentData(request: DeleteSportsRequest): Observable<String> {
        return apiHelper.deleteSportsEquipmentData((request))
    }

    override fun getSavedSportsEquipments(request: GetSavedModelEquipmentRequest): Observable<GetSavedModelEquipmentResponse> {
        return apiHelper.getSavedSportsEquipments(request)
    }

    override fun checkChallengeNameAvailable(request: ChallengeNameRequest): Observable<ChallengeNameResponse> {
        return apiHelper.checkChallengeNameAvailable(request)
    }

    override fun checkSportEquipmentAddedOrNot(): Observable<CheckSportsEquipmentAddedOrNotResponse> {
        return apiHelper.checkSportEquipmentAddedOrNot()
    }
}