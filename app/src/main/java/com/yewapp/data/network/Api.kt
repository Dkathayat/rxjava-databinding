package com.yewapp.data.network

import com.yewapp.data.network.api.FollowUpdateResponse
import com.yewapp.data.network.api.FollowersUpdateResponse
import com.yewapp.data.network.api.ManageUserListResponse
import com.yewapp.data.network.api.about.CmsResponse
import com.yewapp.data.network.api.addmodelequipment.AddModelEquipmentRequest
import com.yewapp.data.network.api.addmodelequipment.AddModelEquipmentResponse
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentResponse
import com.yewapp.data.network.api.addmodelequipment.GetSavedModelEquipmentRequest
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
import com.yewapp.data.network.api.signup.SignUpRequest
import com.yewapp.data.network.api.signup.SignUpRequestSocial
import com.yewapp.data.network.api.signup.SignUpResponse
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
import io.reactivex.Observable
import retrofit2.http.*


interface Api {

    @POST(SIGN_UP)
    fun signUp(
        @Body request: SignUpRequest
    ): Observable<GenericResponse<SignUpResponse>>

    @POST(SOCIAL_LOGIN)
    fun signUpSocial(
        @Body request: SignUpRequestSocial
    ): Observable<GenericResponse<SignUpResponse>>

    @POST(VERIFY_OTP)
    fun verifyCode(
        @Body request: VerifyRequest
    ): Observable<GenericResponse<SignUpResponse>>

    @POST(FORGOT_PASSWORD)
    fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Observable<GenericResponse<ForgotPasswordResponse>>

    @POST(CHANGE_PASSWORD)
    fun changePassword(
        @Body request: ChangePasswordRequest
    ): Observable<GenericResponse<Any>>

    @POST(LOGIN)
    fun login(
        @Body request: LoginRequest
    ): Observable<GenericResponse<SignUpResponse>>

    @POST(LOGOUT)
    fun logout(): Observable<GenericResponse<Any>>

    @POST(CONTACT_US)
    fun contactUs(
        @Body request: ContactRequest
    ): Observable<GenericResponse<Any>>

    @GET(FOLLOWER)
    fun getFollowerListing(
    ): Observable<GenericResponse<FollowerListingResponse>>

    @GET(FOLLOWING)
    fun getFollowingListing(
    ): Observable<GenericResponse<FollowingListingResponse>>

    @GET(PROFILE)
    fun getProfile(
    ): Observable<GenericResponse<SignUpResponse>>

    @GET(CMS)
    fun getCmsPage(@Path("slug") link: String): Observable<GenericResponse<CmsResponse>>

    @GET(FAQS)
    fun faqs(
    ): Observable<GenericResponse<List<FaqData>>>

    @GET(COUNTRIES)
    fun getCountries(): Observable<GenericResponse<CountryListResponse>>

    @GET(STATES)
    fun getState(
        @Path("countryId") id: String
    ): Observable<GenericResponse<List<State>>>

    @GET(CITY)
    fun getCities(
        @Path("stateId") id: String
    ): Observable<GenericResponse<List<City>>>

    @PUT(UPDATE_PROFILE)
    fun updateProfile(
        @Body request: ProfileRequest
    ): Observable<GenericResponse<SignUpResponse>>

    @PUT(UPDATE_PROFILE_IMAGE)
    fun updateProfileImages(
        @Body request: ProfileImageRequest
    ): Observable<GenericResponse<Any>>

    @POST(FEED)
    fun createFeed(
        @Body request: CreateFeedRequest
    ): Observable<GenericResponse<Any>>

    @GET(FEED)
    fun getFeed(
        @Query("search") search: String,
        @Query("userId") userId: Int?,
        @Query("startDate") startDate: String, //yyyy-mm-dd
        @Query("endDate") endDate: String,
        @Query("page") currentPage: Int
    ): Observable<GenericResponse<FeedResponse>>

    @POST(SEND_FEEDBACK)
    fun sendFeedback(
        @Body request: SendFeedback
    ): Observable<GenericResponse<Any>>

    @POST(DATE_FILTER_ACTIVITIES)
    fun getFilteredActivitiesData(
        @Body request: ActivitiesFilterRequest
    ): Observable<GenericResponse<ActivitiesFilterResponseData>>

    @POST(POST_ACTIVITY_LIKE)
    fun postActivityLike(
        @Query("activityId") activityID:String,
        @Query("likeUnicode") likeUniCode:String
    ) : Observable<GenericResponse<Any>>

    @POST(REFERRAL)
    fun sendRefer(
        @Body request: MutableList<PhoneContacts>
    ): Observable<GenericResponse<Any>>

    @GET(REFERRAL)
    fun referFriendList(
    ): Observable<GenericResponse<List<ReferResponse>>>

    @GET(REFERRAL_REWARD_HISTORY)
    fun referRewardHistory(
        @Query("page") currentPage: Int
    ): Observable<GenericResponse<ReferRewardHistoryResponse>>

    @GET(REASON)
    fun reportReasonList(
    ): Observable<GenericResponse<List<ReportReason>>>

    @POST(REPORT)
    fun sendReport(
        @Body request: ReportPost
    ): Observable<GenericResponse<Any>>

    @POST(REPORT_FOR_COMMENT)
    fun sendReportForComment(
        @Body request: CommentReportPost
    ): Observable<GenericResponse<Any>>

    @GET(REPORT_FOR_COMMENT)
    fun getFeedReportedComments(): Observable<GenericResponse<GetFeedReportedComments>>


    @POST(SEND_FEED_REPORT)
    fun sendFeedReport(
        @Body request: SendFeedReport
    ): Observable<GenericResponse<Any>>

    @GET(GET_FEED_REPORT)
    fun getReportedFeeds(): Observable<GenericResponse<GetReportedPost>>

    @GET(GET_SHORT_REPORT_VIDEO)
    fun getReportedShortVideos(): Observable<GenericResponse<GetShortReportedVideo>>


    @DELETE(DELETE_REPORTED_COMMENTS)
    fun deleteReportedComment(
        @Query("commentId") request: Int
    ): Observable<GenericResponse<Any>>

    @DELETE(DELETE_REPORTED_FEED)
    fun deleteReportedFeed(
        @Query("feedId") request: Int
    ): Observable<GenericResponse<Any>>

    @POST(BLOCK_USER)
    fun blockUser(
        @Body request: BlockUserRequest
    ): Observable<GenericResponse<Any>>

    @POST(FAVOURITE)
    fun favouriteUser(
        @Body request: BlockUserRequest
    ): Observable<GenericResponse<Any>>

    @POST(MUTE_USER)
    fun muteUser(
        @Body request: BlockUserRequest
    ): Observable<GenericResponse<Any>>

    @POST(FOLLOW_USER)
    fun followUser(
        @Body request: BlockUserRequest
    ): Observable<GenericResponse<FollowUpdateResponse>>
//    fun followerFollowUser(request: BlockUserRequest): Observable<FollowersUpdateResponse>

    @POST(FOLLOW_USER)
    fun followUserFeed(
        @Body request: BlockUserRequest
    ): Observable<GenericResponse<Any>>

    @POST(FOLLOW_USER)
    fun followFollowerUser(
        @Body request: BlockUserRequest
    ): Observable<GenericResponse<FollowersUpdateResponse>>

    @POST(LIKE_FEED)
    fun likeFeed(
        @Body request: LikeFeedRequest
    ): Observable<GenericResponse<Feed>>

    @POST(COMMENT)
    fun comment(
        @Body request: CommentRequest
    ): Observable<GenericResponse<CommentListResponse>>

    @GET(GET_COMMENTS)
    fun commentList(
        @Query("feedId") feedId: Int?
    ): Observable<GenericResponse<CommentResponse>>

    @GET(GET_LIKES)
    fun feedsLikedList(
        @Query("feedId") feedId: Int?
    ): Observable<GenericResponse<FeedLikeResponse>>

    @GET(GET_SPORT_TYPE)
    fun getSportType(): Observable<GenericResponse<List<SportType>>>

    @GET(SPORTS_LIST)
    fun getSportsList(
    ): Observable<GenericResponse<List<Sport>>>

    @GET(PROFILE_SPORTS)
    fun getProfileSportsList(): Observable<GenericResponse<List<ProfileSportsResponse>>>

    @GET(PROFILE_SPORTS)
    fun getProfileSportsSubSportList(
        @Query("") sportId:Int
    ): Observable<GenericResponse<List<ProfileSportsResponse>>>

    @GET(SUB_SPORTS_LIST)
    fun getSubSportsList(
        @Path("parentId") parentSportId: Int
    ): Observable<GenericResponse<List<Sport>>>

    @GET(SPORTS_MODEL_LIST)
    fun getSportsModelList(
        @Path("subSportId") subSportId: Int
    ): Observable<GenericResponse<List<Model>>>

    @GET(ROUTES_LIST)
    fun getRoutesList(
        @Path("type") type: String
    ): Observable<GenericResponse<RouteListResponse>>

    @GET(ROUTES_DETAIL)
    fun getRoutesDetail(
        @Path("id") id: Int
    ): Observable<GenericResponse<RouteDetailResponse>>

    @GET(INVITE_MEMBER_LIST)
    fun getInviteMemberList(): Observable<GenericResponse<InviteMemberListResponse>>//@Query("age") age: Int

    @POST(CREATE_CHALLENGE)
    fun createChallenge(
        @Body request: CreateChallengeRequest
    ): Observable<GenericResponse<CreateChallengeResponse>>

    @POST(MARK_FAVORITES)
    fun addToFavorites(
        @Path("id") id: Int
    ): Observable<GenericResponse<Any>>

    @POST(NOTIFICATION_SETTING)
    fun addSettingsNotification(
        @Body request: NotificationSettingRequest
    ): Observable<GenericResponse<Any>>

    @GET(GET_NOTIFICATION_SETTING)
    fun getSettingsNotification(
    ): Observable<GenericResponse<NotificationSettingRequest>>

    @POST(VIDEO_FEED)
    fun saveVideo(@Body saveVideoRequest: SaveVideoRequest): Observable<GenericResponse<Any>>

    @GET(VIDEO_FEED)
    fun getPublishVideoList(): Observable<GenericResponse<VideoListResponse>>

    @GET(VIDEO_FEED_FOR_USER)
    fun getUserPublishVideoList(
        @Query("search") search: String,
        @Query("userId") userId: Int
    ): Observable<GenericResponse<VideoListResponse>>

    @GET(VIDEO_COMMENT)
    fun getCommentList(@Query("feedId") feedId: Int): Observable<GenericResponse<CommentListResponse>>

    @GET(GET_BLOCKED_USER_LIST)
    fun getBlockManageUserList(
    ): Observable<GenericResponse<ManageUserListResponse>>

    @GET(GET_MUTED_USER_LIST)
    fun getMutedManageUserList(
    ): Observable<GenericResponse<ManageUserListResponse>>

    @GET(GET_REPORTED_USER_LIST)
    fun getReportedManageUserList(
    ): Observable<GenericResponse<ManageUserListResponse>>


    @GET(GET_FAVORITES_USER_LIST)
    fun getFavoriteManageUserList(
    ): Observable<GenericResponse<ManageUserListResponse>>

    @POST(CHANGE_EMAIL)
    fun changeEmail(@Body request: ChangeEmailRequest): Observable<GenericResponse<Any>>

    @POST(CHANGE_PHONE)
    fun changePhone(@Body request: ChangePhoneRequest): Observable<GenericResponse<Any>>

    @POST(UPDATE_EMAIL)
    fun updateEmail(@Body request: UpdateEmailRequest): Observable<GenericResponse<Any>>

    @POST(UPDATE_PHONE)
    fun updatePhone(@Body request: UpdatePhoneRequest): Observable<GenericResponse<Any>>

    @POST(LIKE_FEED)
    fun likeVideoFeed(
        @Body request: LikeFeedRequest
    ): Observable<GenericResponse<VideoData>>

    @POST(LIKE_COMMENT)
    fun likeCommentFeed(@Body likeCommentRequest: LikeCommentRequest): Observable<GenericResponse<CommentListResponse>>


    @GET(GET_FAVORITES_USER_LIST)
    fun getFavoriteUserList(
    ): Observable<GenericResponse<FavoritesUserResponse>>


    @GET(GET_SUGGESTED_USER_LIST)
    fun getSuggestedUserList(
    ): Observable<GenericResponse<SuggestedUserResponse>>

    @GET(GET_ALL_USER_LIST)
    fun getALLUserList(
        @Query("search") search: String, @Query("page") currentPage: Int
    ): Observable<GenericResponse<ManageUserListResponse>>


    @GET(GET_VIDEO_FEED_FOLLOWER_LIST)
    fun getVideoFeedFollowerList(

    ): Observable<GenericResponse<FollowerListingResponse>>  // @Query("userId") userId: Int

    @GET(GET_VIDEO_FEED_FOLLOWING_LIST)
    fun getVideoFeedFollowingList(

    ): Observable<GenericResponse<FollowerListingResponse>>// @Query("userId") userId: Int @GET(GET_VIDEO_FEED_FOLLOWING_LIST)

    @GET(GET_SUBSCRIPTION_HISTORY)
    fun getSubscriptionHistory(): Observable<GenericResponse<SubscriptionHistory>>

    @GET(GET_SUBSCRIPTION_PLANS)
    fun getSubscriptionPlanList(): Observable<GenericResponse<SubscriptionPlans>>

    //NEW

    @GET(GET_CHALLENGE)
    fun getCommonChallengeList(
        @Query("type") type: String
    ): Observable<GenericResponse<AllChallengesResponse>>


    @GET(GET_CHALLENGE_DETAIL)
    fun getChallengeDetails(
        @Query("id") challengeId: Int
    ): Observable<GenericResponse<ChallengeDetailResponse>>


    @POST(CREATE_ROUTE)
    fun createRoute(
        @Body request: CreateRouteRequest
    ): Observable<GenericResponse<CreateRouteResponse>>

    @GET(GET_MEMBER_COUNT)
    fun getMaxMemberCount(): Observable<GenericResponse<MaxMemberResponse>>

    @POST(ADD_ASSOCIATE)
    fun addAssociateMember(@Body request: AddAssociateRequest): Observable<GenericResponse<AddAssociateResponse>>


    @POST(UPDATE_ASSOCIATE_MEMBER_DETAILS)
    fun updateAssociateMember(
        @Path("associate_id") associateId: String,
        @Body request: AddAssociateRequest
    ): Observable<GenericResponse<Any>>


    @GET(ASSOCIATE_MEMBERS)
    fun getAssociateMember(): Observable<GenericResponse<AssociateMemberResponse>>

    @POST(VALIDATE_EMAIL)//Email already exist or not
    fun validateAssociateEmail(@Body request: ValidateEmailRequest): Observable<GenericResponse<Any>>


    @GET(ASSOCIATE_PERMISSION)
    fun getAssociatePermissions(
        @Path("associate_id") associateId: String
    ): Observable<GenericResponse<AssociatePermissionResponse>>

    @GET(GET_ASSOCIATE_MEMBER_DETAILS)
    fun getAssociateMemberDetails(
        @Path("associate_id") associateId: String
    ): Observable<GenericResponse<AssociateMemberDetailsResponse>>

    @POST(UPDATE_ASSOCIATE_PERMISSION)
    fun updateAssociatePermission(
        @Body request: AssociatePermissionRequest
    ): Observable<GenericResponse<Any>>

    @POST(MIGRATE_ASSOCIATE_ACCOUNT)
    fun migrateAssociateAccount(
        @Body associateId: MigrateAssociateAccountRequest
    ): Observable<GenericResponse<Any>>

    @POST(REMOVE_ASSOCIATE_ACCOUNT)
    fun removeAssociateAccount(
        @Body removeAssociateAccountRequest: RemoveAssociateAccountRequest
    ): Observable<GenericResponse<Any>>


    @POST(ACTIVATE_DEACTIVATE_ASSOCIATE_ACCOUNT)
    fun activateDeactivateAssociateAccount(
        @Body activeInActiveAssociateAccountRequest: ActiveDeActiveAssociateAccountRequest
    ): Observable<GenericResponse<Any>>

    @POST(ASSOCIATE_SPORTS_TYPE)
    fun getAssociateSportsType(
        @Body associateId: RemoveAssociateAccountRequest
    ): Observable<GenericResponse<AssociateSportsTypeResponse>>

    @POST(SAVE_ASSOCIATE_SPORTS_TYPE)
    fun saveAssociateSportsType(
        @Body request: SaveAssociateSportsTypeRequest
    ): Observable<GenericResponse<Any>>


    @GET(SPECTATOR_MEMBER_LIST)
    fun getSpectatorMembers(): Observable<GenericResponse<SpectatorMemberResponse>>

    @GET(YEW_MEMBERS)
    fun getYewMembersForInvite(): Observable<GenericResponse<YewMemberResponse>>

    @POST(ADD_SPECTATOR)
    fun addSpectator(@Body request: AddSpectatorRequest): Observable<GenericResponse<Any>>

//    @DELETE(DELETE_SPECTATOR)
//    fun deleteSpectator(@Body request: DeleteSpectatorRequest): Observable<GenericResponse<Any>>

    @HTTP(method = "DELETE", path = DELETE_SPECTATOR, hasBody = true)
    fun deleteSpectator(@Body request: DeleteSpectatorRequest): Observable<GenericResponse<Any>>


    @GET(MY_POINTS)
    fun getMyPoints(): Observable<GenericResponse<MyPointResponse>>

    @GET(USER_POINT_SUMMARY)
    fun getUserPointSummary(
        @Query("page") currentPage: Int,
        @Query("category") search: String,
        @Query("startDate") startDate: String, //yyyy-mm-dd
        @Query("endDate") endDate: String,
        @Query("sport_id") sportsId:String,
        @Query("sub_sport") subSportsId:List<String>,

    ): Observable<GenericResponse<UserPointSummaryResponse>>


    @GET(USER_POINT_HISTORY)
    fun getUserPointsHistory(
        @Query("page") currentPage: Int,
        @Query("startDate") startDate: String, //yyyy-mm-dd
        @Query("endDate") endDate: String,
        @Query("sport_id") sportsID: List<String>,
        @Query("sub_sport_id") subSportId: List<String>
    ): Observable<GenericResponse<MyPointsHistoryResponse>>

    @POST(PROFILE_SETTINGS)
    fun updateProfileSettings(
        @Body request: ProfileSettingsRequest
    ): Observable<GenericResponse<Any>>

    @POST(SAVE_USER_SPORTS_EQUIPMENTS)
    fun saveUserSportsEquipments(
        @Body request: AddModelEquipmentRequest
    ): Observable<GenericResponse<List<AddModelEquipmentResponse>>>

    @POST(GET_USER_EQUIPMENTS_LIST)
    fun getSavedSportsEquipments(
        @Body request: GetSavedModelEquipmentRequest
    ): Observable<GenericResponse<GetSavedModelEquipmentResponse>>

    @HTTP(method = "DELETE", path = DELETE_USER_SPORTS_EQUIPMENTS, hasBody = true)
    fun deleteSportsEquipmentData(
        @Body request: DeleteSportsRequest
    ): Observable<GenericResponse<Any>>


    @POST(CHECK_CHALLENGE_NAME_AVAILABLE)
    fun checkChallengeNameAvailable(
        @Body request: ChallengeNameRequest
    ): Observable<GenericResponse<ChallengeNameResponse>>

    @GET(CHECK_SPORTS_EQUIPMENT)
    fun checkSportEquipmentAddedOrNot(
    ): Observable<GenericResponse<CheckSportsEquipmentAddedOrNotResponse>>

}
