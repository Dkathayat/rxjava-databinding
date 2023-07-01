package com.yewapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.util.SparseIntArray
import androidx.annotation.StringRes
import com.yewapp.R
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
import com.yewapp.utils.ApiFailException
import com.yewapp.utils.CONNECTION_ERROR
import com.yewapp.utils.NoNetworkException
import io.reactivex.Observable
import retrofit2.Retrofit
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

object ApiStatus {
    const val SUCCESS = 200
    const val ERROR = 400
    const val UNAUTHORIZED = 401
    const val NOT_FOUND = 404
    const val SERVER_ERROR = 500
    const val SERVICE_UNAVAILABLE = 503

    val API_STATUS_MESSAGE_MAP: SparseIntArray = object : SparseIntArray() {
        init {
            append(SUCCESS, R.string.success)
            append(UNAUTHORIZED, R.string.bad_request)
        }
    }
}

class ApiHelperImpl @Inject constructor(var context: Context, retrofit: Retrofit) : ApiHelper {
    var api: Api = retrofit.create(Api::class.java)

    override fun signUp(request: SignUpRequest): Observable<SignUpResponse> {
        return responseHandler(api.signUp(request))
    }

    override fun signUpSocial(request: SignUpRequestSocial): Observable<SignUpResponse> {
        return responseHandler(api.signUpSocial(request))
    }

    override fun verifyCode(request: VerifyRequest): Observable<SignUpResponse> {
        return responseHandler(api.verifyCode(request))
    }

    override fun forgotPassword(request: ForgotPasswordRequest): Observable<ForgotPasswordResponse> {
        return responseHandler(api.forgotPassword(request))
    }

    override fun changePassword(request: ChangePasswordRequest): Observable<String> {
        return stringResponseHandler(api.changePassword(request))
    }

    override fun login(request: LoginRequest): Observable<SignUpResponse> {
        return responseHandler(api.login(request))
    }

    override fun logout(): Observable<String> {
        return stringResponseHandler(api.logout())
    }

    override fun contactUs(request: ContactRequest): Observable<String> {
        return stringResponseHandler(api.contactUs(request))
    }

    override fun comment(request: CommentRequest): Observable<CommentListResponse> {
        return responseHandler(api.comment(request))
    }

    override fun getFollowerList(): Observable<FollowerListingResponse> {
        return responseHandler(api.getFollowerListing())
    }

    override fun getFollowingList(): Observable<FollowingListingResponse> {
        return responseHandler(api.getFollowingListing())
    }

    override fun getProfile(): Observable<SignUpResponse> {
        return responseHandler(api.getProfile())
    }

    override fun getCountries(): Observable<CountryListResponse> {
        return responseHandler(api.getCountries())
    }

    override fun getStates(id: String): Observable<List<State>> {
        return responseHandler(api.getState(id))
    }

    override fun getCities(id: String): Observable<List<City>> {
        return responseHandler(api.getCities(id))
    }

    override fun updateProfile(request: ProfileRequest): Observable<SignUpResponse> {
        return responseHandler(api.updateProfile(request))
    }

    override fun updateProfileImage(request: ProfileImageRequest): Observable<String> {
        return stringResponseHandler(api.updateProfileImages(request))
    }

    override fun getSportTypes(): Observable<List<SportType>> {
        return responseHandler(api.getSportType())
    }

    override fun createFeed(request: CreateFeedRequest): Observable<String> {
        return stringResponseHandler(api.createFeed(request))
    }

    override fun sendRefer(request: MutableList<PhoneContacts>): Observable<String> {
        return stringResponseHandler(api.sendRefer(request))
    }

    override fun faqs(): Observable<List<FaqData>> {
        return responseHandler(api.faqs())
    }

    override fun referFriendList(): Observable<List<ReferResponse>> {
        return responseHandler(api.referFriendList())
    }

    override fun referRewardHistory(page: Int): Observable<ReferRewardHistoryResponse> {
        return responseHandler(api.referRewardHistory(page))
    }

    override fun postActivityLike(activityId: String, unicode: String): Observable<String> {
        return stringResponseHandler(api.postActivityLike(activityId,unicode))
    }


    override fun commentList(feedId: Int): Observable<CommentResponse> {
        return responseHandler(api.commentList(feedId))
    }

    override fun feedsLikedList(feedId: Int): Observable<FeedLikeResponse> {
        return responseHandler(api.feedsLikedList(feedId))
    }

    override fun getSportsList(): Observable<List<Sport>> {
        return responseHandler(api.getSportsList())
    }

    override fun getProfileSportsSubSportList(sportId: Int): Observable<List<ProfileSportsResponse>> {
        return responseHandler(api.getProfileSportsSubSportList(sportId))
    }

    override fun getProfileSportsList(): Observable<List<ProfileSportsResponse>> {
        return responseHandler(api.getProfileSportsList())
    }

    override fun getSubSportsList(parentSportId: Int): Observable<List<Sport>> {
        return responseHandler(api.getSubSportsList(parentSportId))
    }

    override fun getSubSportsModelList(subSportId: Int): Observable<List<Model>> {
        return responseHandler(api.getSportsModelList(subSportId))
    }

    override fun getSubscriptionHistory(): Observable<SubscriptionHistory> {
        return responseHandler(api.getSubscriptionHistory())
    }

    override fun getSubscriptionPlansList(): Observable<SubscriptionPlans> {
        return responseHandler(api.getSubscriptionPlanList())
    }

    override fun getRouteList(type: String): Observable<RouteListResponse> {
        return responseHandler(api.getRoutesList(type))
    }

    override fun getRouteDetail(id: Int): Observable<RouteDetailResponse> {
        return responseHandler(api.getRoutesDetail(id))
    }

    override fun getInviteMemberList(): Observable<InviteMemberListResponse> {
        return responseHandler(api.getInviteMemberList())
    }

    override fun addToFavorite(id: Int): Observable<String> {
        return stringResponseHandler(api.addToFavorites(id))
    }

    override fun sendFeedReport(request: SendFeedReport): Observable<String> {
        return stringResponseHandler(api.sendFeedReport(request))
    }

    override fun getBlockedManageUserList(): Observable<ManageUserListResponse> {
        return responseHandler(api.getBlockManageUserList())
    }

    override fun getMutedManageUserList(): Observable<ManageUserListResponse> {
        return responseHandler(api.getMutedManageUserList())
    }

    override fun getReportedManageUserList(): Observable<ManageUserListResponse> {
        return responseHandler(api.getReportedManageUserList())
    }

    override fun getFavoriteManageUserList(): Observable<ManageUserListResponse> {
        return responseHandler(api.getFavoriteManageUserList())
    }

    override fun createChallenge(request: CreateChallengeRequest): Observable<CreateChallengeResponse> {
        return responseHandler(api.createChallenge(request))
    }

    override fun createRoute(request: CreateRouteRequest): Observable<CreateRouteResponse> {
        return responseHandler(api.createRoute(request))
    }

    override fun getMaxMemberCount(): Observable<MaxMemberResponse> {
        return responseHandler(api.getMaxMemberCount())
    }

    override fun addAssociateMember(request: AddAssociateRequest): Observable<AddAssociateResponse> {
        return responseHandler(api.addAssociateMember(request))
    }

    override fun updateAssociateMember(
        associateId: String,
        request: AddAssociateRequest
    ): Observable<String> {
        return stringResponseHandler(api.updateAssociateMember(associateId, request))
    }

    override fun getAssociateMember(): Observable<AssociateMemberResponse> {
        return responseHandler(api.getAssociateMember())
    }

    override fun validateAssociateEmail(request: ValidateEmailRequest): Observable<String> {
        return stringResponseHandler(api.validateAssociateEmail(request))
    }

    override fun getAssociatePermissions(associateId: String): Observable<AssociatePermissionResponse> {
        return responseHandler(api.getAssociatePermissions(associateId))
    }

    override fun getAssociateMemberDetails(associateId: String): Observable<AssociateMemberDetailsResponse> {
        return responseHandler(api.getAssociateMemberDetails(associateId))
    }

    override fun updateAssociatePermission(request: AssociatePermissionRequest): Observable<String> {
        return stringResponseHandler(api.updateAssociatePermission(request))
    }

    override fun migrateAssociateAccount(request: MigrateAssociateAccountRequest): Observable<String> {
        return stringResponseHandler(api.migrateAssociateAccount(request))
    }

    override fun removeAssociateAccount(request: RemoveAssociateAccountRequest): Observable<String> {
        return stringResponseHandler(api.removeAssociateAccount(request))
    }

    override fun activateDeactivateAssociateAccount(request: ActiveDeActiveAssociateAccountRequest): Observable<String> {
        return stringResponseHandler(api.activateDeactivateAssociateAccount(request))
    }

    override fun getAssociateSportsType(request: RemoveAssociateAccountRequest): Observable<AssociateSportsTypeResponse> {
        return responseHandler(api.getAssociateSportsType(request))
    }

    override fun saveAssociateSportsType(request: SaveAssociateSportsTypeRequest): Observable<String> {
        return stringResponseHandler(api.saveAssociateSportsType(request))
    }

    override fun getYewMembersForInvite(): Observable<YewMemberResponse> {
        return responseHandler(api.getYewMembersForInvite())
    }

    override fun addSpectator(request: AddSpectatorRequest): Observable<String> {
        return stringResponseHandler(api.addSpectator(request))
    }

    override fun getSpectatorMembers(): Observable<SpectatorMemberResponse> {
        return responseHandler(api.getSpectatorMembers())
    }

    override fun deleteSpectator(request: DeleteSpectatorRequest): Observable<String> {
        return stringResponseHandler(api.deleteSpectator(request))
    }

    override fun getMyPoints(): Observable<MyPointResponse> {
        return responseHandler(api.getMyPoints())
    }

    override fun getUserPointSummary(
        page: Int,
        category: String,
        startDate: String,
        endDate: String,
        sportid:String,
        subSportId: List<String>
    ): Observable<UserPointSummaryResponse> {
        return responseHandler(api.getUserPointSummary(page, category, startDate, endDate,sportid,subSportId))
    }

    override fun getUserHistorySummary(
        page: Int,
        startDate: String,
        endDate: String,
        sportsId: List<String>,
        subSportId: List<String>
    ): Observable<MyPointsHistoryResponse> {
        return responseHandler(
            api.getUserPointsHistory(
                page,
                startDate,
                endDate,
                sportsId,
                subSportId
            )
        )
    }

    override fun updateProfileSettings(request: ProfileSettingsRequest): Observable<String> {
        return stringResponseHandler(api.updateProfileSettings(request))
    }

    override fun getSavedSportsEquipments(request: GetSavedModelEquipmentRequest): Observable<GetSavedModelEquipmentResponse> {
        return responseHandler(api.getSavedSportsEquipments(request))
    }

    override fun saveUserSportsEquipments(request: AddModelEquipmentRequest): Observable<List<AddModelEquipmentResponse>> {
        return responseHandler(api.saveUserSportsEquipments(request))
    }

    override fun deleteSportsEquipmentData(request: DeleteSportsRequest): Observable<String> {
        return stringResponseHandler(api.deleteSportsEquipmentData(request))
    }

    override fun checkChallengeNameAvailable(request: ChallengeNameRequest): Observable<ChallengeNameResponse> {
        return responseHandler(api.checkChallengeNameAvailable(request))
    }

    override fun checkSportEquipmentAddedOrNot(): Observable<CheckSportsEquipmentAddedOrNotResponse> {
        return responseHandler(api.checkSportEquipmentAddedOrNot())
    }


    override fun setNotificationSetting(notificationSettingRequest: NotificationSettingRequest): Observable<String> {
        return stringResponseHandler(api.addSettingsNotification(notificationSettingRequest))
    }

    override fun getNotificationSetting(): Observable<NotificationSettingRequest> {
        return responseHandler(api.getSettingsNotification())
    }

    override fun reportReasonList(): Observable<List<ReportReason>> {
        return responseHandler(api.reportReasonList())
    }

    override fun sendReport(request: ReportPost): Observable<String> {
        return stringResponseHandler(api.sendReport(request))
    }

    override fun getPostReported(): Observable<GetReportedPost> {
        return responseHandler(api.getReportedFeeds())
    }

    override fun getReportedShortVideos(): Observable<GetShortReportedVideo> {
        return responseHandler(api.getReportedShortVideos())
    }

    override fun deleteReportedComments(request: Int): Observable<String> {
        return stringResponseHandler(api.deleteReportedComment(request))
    }

    override fun deleteReportedFeed(request: Int): Observable<String> {
        return stringResponseHandler(api.deleteReportedFeed(request))
    }

    override fun sendReportForComment(request: CommentReportPost): Observable<String> {
        return stringResponseHandler(api.sendReportForComment(request))
    }

    override fun getReportedFeedComment(): Observable<GetFeedReportedComments> {
        return responseHandler(api.getFeedReportedComments())
    }


    override fun getCmsPage(link: String): Observable<CmsResponse> {
        return responseHandler(api.getCmsPage(link))
    }

    override fun sendFeedback(request: SendFeedback): Observable<String> {
        return stringResponseHandler(api.sendFeedback(request))
    }

    override fun getFeed(
        search: String,
        userId: Int?,
        startDate: String,
        endDate: String,
        currentPage: Int,
    ): Observable<FeedResponse> {
        return responseHandler(api.getFeed(search, userId, startDate, endDate, currentPage))
    }

    override fun likeFeed(request: LikeFeedRequest): Observable<Feed> {
        return responseHandler(api.likeFeed(request))
    }


    override fun blockUser(request: BlockUserRequest): Observable<String> {
        return stringResponseHandler(api.blockUser(request))
    }

    override fun favouriteUser(request: BlockUserRequest): Observable<String> {
        return stringResponseHandler(api.favouriteUser(request))
    }

    override fun muteUser(request: BlockUserRequest): Observable<String> {
        return stringResponseHandler(api.muteUser(request))
    }

    override fun followUser(request: BlockUserRequest): Observable<FollowUpdateResponse> {
        return responseHandler(api.followUser(request))
    }

    override fun followUserFeed(request: BlockUserRequest): Observable<String> {
        return stringResponseHandler(api.followUserFeed(request))
    }

    override fun followerFollowUser(request: BlockUserRequest): Observable<FollowersUpdateResponse> {
        return responseHandler(api.followFollowerUser(request))
    }

    override fun getFilterActivityData(request: ActivitiesFilterRequest): Observable<ActivitiesFilterResponseData> {
        return responseHandler(api.getFilteredActivitiesData(request))
    }

    override fun saveVideo(saveVideoRequest: SaveVideoRequest): Observable<Any> {
        return responseHandler(api.saveVideo(saveVideoRequest))
    }

    override fun getPublishVideoList(): Observable<VideoListResponse> {
        return responseHandler(api.getPublishVideoList())
    }

    override fun getCommentList(feedId: Int): Observable<CommentListResponse> {
        return responseHandler(api.getCommentList(feedId))
    }

    override fun changeEmail(request: ChangeEmailRequest): Observable<String> {
        return stringResponseHandler(api.changeEmail(request))
    }

    override fun changePhone(request: ChangePhoneRequest): Observable<String> {
        return stringResponseHandler(api.changePhone(request))
    }

    override fun updateEmail(request: UpdateEmailRequest): Observable<String> {
        return stringResponseHandler(api.updateEmail(request))
    }

    override fun updatePhone(request: UpdatePhoneRequest): Observable<String> {
        return stringResponseHandler(api.updatePhone(request))
    }

    override fun likeVideFeed(request: LikeFeedRequest): Observable<VideoData> {
        return responseHandler(api.likeVideoFeed(request))
    }

    override fun likeComment(likeCommentRequest: LikeCommentRequest): Observable<CommentListResponse> {
        return responseHandler(api.likeCommentFeed(likeCommentRequest))
    }

    override fun getFavoritesUserList(): Observable<FavoritesUserResponse> {
        return responseHandler(api.getFavoriteUserList())
    }

    override fun getSuggestedUserList(): Observable<SuggestedUserResponse> {
        return responseHandler(api.getSuggestedUserList())
    }

    override fun getAllUserList(
        search: String, currentPage: Int
    ): Observable<ManageUserListResponse> {
        return responseHandler(api.getALLUserList(search, currentPage))
    }

    override fun getUserPublishVideoList(
        search: String?, userId: Int
    ): Observable<VideoListResponse> {
        return responseHandler(api.getUserPublishVideoList(search.toString(), userId))
    }

    override fun videoFeedFollowerList(): Observable<FollowerListingResponse> {//userId: Int
        return responseHandler(api.getVideoFeedFollowerList())//userId
    }

    override fun videoFeedFollowingList(): Observable<FollowerListingResponse> {//userId: Int
        return responseHandler(api.getVideoFeedFollowingList())//userId
    }

    override fun getCommonChallengeList(type: String): Observable<AllChallengesResponse> {
        return responseHandler(api.getCommonChallengeList(type))
    }

    override fun getChallengeDetails(challengeId: Int): Observable<ChallengeDetailResponse> {
        return responseHandler(api.getChallengeDetails(challengeId))
    }


    private fun <T> responseHandler(response: Observable<GenericResponse<T>>): Observable<T> {
        return isInternetOn().doOnNext(this::handleNetworkStatus).switchMap {
            response.doOnError(this::onHttpException).doOnNext(this::validateResponse)
                .map(GenericResponse<T>::data)
        }
    }

    private fun <T> stringResponseHandler(response: Observable<GenericResponse<T>>): Observable<String> {
        return isInternetOn().doOnNext(this::handleNetworkStatus).switchMap {
            response.doOnError(this::onHttpException).doOnNext(this::validateResponse)
                .map(GenericResponse<T>::message)
        }
    }

    @Throws(ApiFailException::class)
    fun onHttpException(throwable: Throwable) {
        Log.i("Error", throwable.localizedMessage)
        if (throwable is SocketTimeoutException) {
            throw NoNetworkException(
                CONNECTION_ERROR, R.string.server_maintenance
            )
        }
        if (throwable is UnknownHostException) {
            throw NoNetworkException(
                CONNECTION_ERROR, R.string.server_maintenance
            )
        }
        if (throwable is ConnectException) {
            throw NoNetworkException(
                CONNECTION_ERROR, R.string.error_no_internet_connection
            )
        }
    }

    @Throws(ApiFailException::class)
    private fun <T> validateResponse(response: GenericResponse<T>?) {
        if (response == null) {
            throw ApiFailException()
        }
        if (response.code != ApiStatus.SUCCESS) {
            var messageRes: Int = getResponseMessageRes(response)
            val message: String
            if (messageRes == 0) {
                message = response.message
                messageRes = R.string.success
            } else {
                message = context.getString(messageRes)
            }
            throw ApiFailException(
                response.code, message, messageRes
            )
        }
    }


    @StringRes
    private fun <T> getResponseMessageRes(response: GenericResponse<T>): Int {
        return ApiStatus.API_STATUS_MESSAGE_MAP.get(response.code)
    }

    private fun handleNetworkStatus(networkStatus: Boolean) {
        val CONNECTION_ERROR =
            "Failed to connect. Make sure you have an active internet connection."
        if (!networkStatus) {
            throw NoNetworkException(
                CONNECTION_ERROR, R.string.error_no_internet_connection
            )
        }
    }

    private fun isInternetOn(): Observable<Boolean> {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return Observable.just(manager?.activeNetworkInfo?.isConnected ?: false)
    }
}
