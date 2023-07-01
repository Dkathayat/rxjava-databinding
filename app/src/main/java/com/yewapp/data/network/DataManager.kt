package com.yewapp.data.network

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
import com.yewapp.data.network.api.signup.SubscriptionDetail
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
import retrofit2.http.Body

interface DataManager {

    //Pre-Login Network Calls
    fun signup(request: SignUpRequest): Observable<SignUpResponse>

    fun signUpSocial(request: SignUpRequestSocial): Observable<SignUpResponse>
    fun verifyCode(request: VerifyRequest): Observable<SignUpResponse>
    fun forgotPassword(request: ForgotPasswordRequest): Observable<ForgotPasswordResponse>
    fun changePassword(request: ChangePasswordRequest): Observable<String>
    fun login(request: LoginRequest): Observable<SignUpResponse>

    //Post-Login Network Calls
    fun contactUs(request: ContactRequest): Observable<String>
    fun comment(request: CommentRequest): Observable<CommentListResponse>
    fun logout(): Observable<String>
    // save aws credentials
    //  fun saveAwsCredentials(user: AwsCredential?)

    //Followers
    fun getFollowerList(): Observable<FollowerListingResponse>
    fun getFollowingList(): Observable<FollowingListingResponse>

    //Profile
    fun getProfile(): Observable<SignUpResponse>
    fun getCountries(): Observable<CountryListResponse>
    fun getStates(id: String): Observable<List<State>>
    fun getCities(id: String): Observable<List<City>>
    fun updateProfile(request: ProfileRequest): Observable<SignUpResponse>
    fun getSportTypes(): Observable<List<SportType>>

    //Feeds
    fun createFeed(request: CreateFeedRequest): Observable<String>
    fun faqs(): Observable<List<FaqData>>
    fun referFriendList(): Observable<List<ReferResponse>>
    fun referRewardHistory(page: Int): Observable<ReferRewardHistoryResponse>
    fun postActivityLike(activtyId:String,unicode:String): Observable<String>

    fun commentList(feedId: Int): Observable<CommentResponse>
    fun feedsLikedList(feedId: Int): Observable<FeedLikeResponse>
    fun getFeed(
        search: String,
        userId: Int?,
        startDate: String,
        endDate: String,
        currentPage: Int,
    ): Observable<FeedResponse>

    fun blockUser(request: BlockUserRequest): Observable<String>
    fun sendFeedback(request: SendFeedback): Observable<String>
    fun favouriteUser(request: BlockUserRequest): Observable<String>
    fun muteUser(request: BlockUserRequest): Observable<String>
    fun followUserFeed(request: BlockUserRequest): Observable<String>

    fun followUser(request: BlockUserRequest): Observable<FollowUpdateResponse>
    fun followerFollowUser(request: BlockUserRequest): Observable<FollowersUpdateResponse>
    fun getActivityFilterData(request: ActivitiesFilterRequest):Observable<ActivitiesFilterResponseData>

    fun likeFeed(request: LikeFeedRequest): Observable<Feed>
    fun likeVideFeed(request: LikeFeedRequest): Observable<VideoData>

    fun getSubscriptionHistory(): Observable<SubscriptionHistory>

    fun getSubscriptionPlansDetails(): Observable<SubscriptionPlans>

    // add challenge apis
    fun getSportsList(): Observable<List<Sport>>
    fun getProfileSportsList():Observable<List<ProfileSportsResponse>>
    fun getProfileSportsSubSportList(sportId: Int):Observable<List<ProfileSportsResponse>>
    fun getSubSportsList(parentSportId: Int): Observable<List<Sport>>
    fun getRouteList(type: String): Observable<RouteListResponse>
    fun getInviteMemberList(): Observable<InviteMemberListResponse>
    fun getSubSportsModelList(subSportId: Int): Observable<List<Model>>
    fun getRouteDetail(id: Int): Observable<RouteDetailResponse>
    fun addToFavorite(id: Int): Observable<String>

    //create challenge
    fun createChallenge(request: CreateChallengeRequest): Observable<CreateChallengeResponse>
    fun createRoute(request: CreateRouteRequest): Observable<CreateRouteResponse>

    //Settings
    fun reportReasonList(): Observable<List<ReportReason>>
    fun sendReport(request: ReportPost): Observable<String>

    fun getFeedReports(): Observable<GetReportedPost>
    fun getReportedShortVideos(): Observable<GetShortReportedVideo>
    fun sendReportForComment(request: CommentReportPost): Observable<String>

    fun deleteReportedComment(request: Int): Observable<String>
    fun deleteReportedFeed(request: Int): Observable<String>

    fun getReportedFeedComments(): Observable<GetFeedReportedComments>

    fun getCmsPages(link: String): Observable<CmsResponse>
    fun sendRefer(request: MutableList<PhoneContacts>): Observable<String>

    fun setNotificationSetting(notificationSettingRequest: NotificationSettingRequest): Observable<String>
    fun getNotificationSetting(): Observable<NotificationSettingRequest>
    fun saveVideo(saveVideoRequest: SaveVideoRequest): Observable<Any>
    fun getPublishVideoList(): Observable<VideoListResponse>
    fun getUserPublishVideoList(search: String?, userId: Int): Observable<VideoListResponse>

    fun getCommentList(feedId: Int): Observable<CommentListResponse>

    fun getBlockedManageUserList(): Observable<ManageUserListResponse>

    fun getMutedManageUserList(): Observable<ManageUserListResponse>
    fun getReportedManageUserList(): Observable<ManageUserListResponse>
    fun getFavoriteManageUserList(): Observable<ManageUserListResponse>

    //sendFeedReport
    fun sendFeedReport(request: SendFeedReport): Observable<String>

    //Local Resource Calls
    fun setUserOnBoarded(onBoard: Boolean)
    fun getUserOnBoarded(): Boolean
    fun setLoginStatus(loginStatus: Boolean)
    fun getLoginStatus(): Boolean
    fun getPreference(): PreferencesHelper
    fun getResourceProvider(): ResourceProvider
    fun saveUser(response: Profile?)
    fun getUser(): Profile
    fun saveSubscription(subscriptionDetail: SubscriptionDetail?)
    fun getSubscription(): SubscriptionDetail

    fun saveAccessToken(accessToken: String)
    fun saveAwsCredential(awsCredential: AwsCredential)
    fun getAwsCredential(): AwsCredential
    fun getAccessToken(): String
    fun changeEmail(request: ChangeEmailRequest): Observable<String>
    fun changePhone(request: ChangePhoneRequest): Observable<String>
    fun updateEmail(request: UpdateEmailRequest): Observable<String>
    fun updatePhone(request: UpdatePhoneRequest): Observable<String>

    //like feed comment
    fun likeComment(likeCommentRequest: LikeCommentRequest): Observable<CommentListResponse>
    fun getFavoritesUserList(): Observable<FavoritesUserResponse>
    fun getSuggestedUserList(): Observable<SuggestedUserResponse>
    fun getAllUserList(search: String, currentPage: Int): Observable<ManageUserListResponse>

    //video feeds follower listing
    fun videoFeedFollowerList(): Observable<FollowerListingResponse>//userId: Int
    fun videoFeedFollowingList(): Observable<FollowerListingResponse>//userId: Int


    fun getCommonChallengeList(type: String): Observable<AllChallengesResponse>//userId: Int

    fun getChallengeDetails(challengeId: Int): Observable<ChallengeDetailResponse>//userId: Int

    fun getMaxMemberCount(): Observable<MaxMemberResponse>
    fun addAssociateMember(@Body request: AddAssociateRequest): Observable<AddAssociateResponse>
    fun updateAssociateMember(
        associateId: String,
        @Body request: AddAssociateRequest
    ): Observable<String>

    fun getAssociateMember(): Observable<AssociateMemberResponse>

    fun validateAssociateEmail(@Body request: ValidateEmailRequest): Observable<String>
    fun getAssociatePermissions(associateId: String): Observable<AssociatePermissionResponse>

    fun getAssociateMemberDetails(associateId: String): Observable<AssociateMemberDetailsResponse>

    //Associate options
    fun updateAssociatePermission(@Body request: AssociatePermissionRequest): Observable<String>
    fun migrateAssociateAccount(@Body request: MigrateAssociateAccountRequest): Observable<String>
    fun removeAssociateAccount(@Body request: RemoveAssociateAccountRequest): Observable<String>
    fun activateDeactivateAssociateAccount(@Body request: ActiveDeActiveAssociateAccountRequest): Observable<String>
    fun getAssociateSportsType(request: RemoveAssociateAccountRequest): Observable<AssociateSportsTypeResponse>
    fun saveAssociateSportsType(request: SaveAssociateSportsTypeRequest): Observable<String>

    //Spectator
    fun addSpectator(request: AddSpectatorRequest): Observable<String>
    fun getYewMembersForInvite(): Observable<YewMemberResponse>
    fun getSpectatorMembers(): Observable<SpectatorMemberResponse>
    fun deleteSpectator(request: DeleteSpectatorRequest): Observable<String>

    //MY POINTS
    fun getMyPoints(): Observable<MyPointResponse>
    fun getUserPointSummary(
        page: Int,
        category: String,
        startDate: String,
        endDate: String,
        sportsId: String,
        subSportsId: List<String>
    ): Observable<UserPointSummaryResponse>

    fun getUserPointsHistory(
        page: Int,
        startDate: String,
        endDate: String,
        sportsId: List<String>,
        subSportId: List<String>
    ): Observable<MyPointsHistoryResponse>

    fun updateProfileSettings(request: ProfileSettingsRequest): Observable<String>

    fun updateProfileImage(request: ProfileImageRequest): Observable<String>

    fun saveUserSportsEquipments(request: AddModelEquipmentRequest): Observable<List<AddModelEquipmentResponse>>
    fun deleteSportsEquipmentData(request: DeleteSportsRequest): Observable<String>
    fun getSavedSportsEquipments(request: GetSavedModelEquipmentRequest): Observable<GetSavedModelEquipmentResponse>

    fun checkChallengeNameAvailable(request: ChallengeNameRequest): Observable<ChallengeNameResponse>

    fun checkSportEquipmentAddedOrNot(): Observable<CheckSportsEquipmentAddedOrNotResponse>

}