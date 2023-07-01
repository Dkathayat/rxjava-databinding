package com.yewapp.ui.modules.player.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.video.SaveVideoRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.player.navigator.PlayerVideoNavigator
import com.yewapp.utils.rx.SchedulerProvider
import java.util.regex.Pattern

class PlayerVideoViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<PlayerVideoNavigator>(dataManager, schedulerProvider) {

    var locationRadiusArray = arrayOf("Visible to all", "Friends")
    var selectedChallengeArea = ObservableField<String>("")
    var captionText = ObservableField("")
    var title = ObservableField("")
    var titleError = ObservableField("")
    var captionError = ObservableField("")
    var visibility = ""

    private var isPlay = false
    fun onClickCancel() {
        getNavigator()?.onBackPress()
    }


    var awsCredentials: BasicAWSCredentials

    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )
    }

    override fun setData(extras: Bundle?) {

    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.tvPublish -> {
                if (selectedChallengeArea.get().equals("Friends")) {
                    visibility = "Private"
                } else {
                    visibility = "Public"
                }

                if (!validate()) return
                isLoading.set(true)
                getNavigator()?.navigateToUplish()
            }

            // R.id.tvPublish->callPublishAPI("")
            R.id.challenge_area_edt -> getNavigator()?.onChallengeAreaClick(view)
            R.id.bntPlay -> {
                isPlay = !isPlay
                if (isPlay) {
                    getNavigator()?.pause()
                } else {
                    getNavigator()?.play()
                }
            }
        }
    }


    fun callPublishAPI(videoURL: String) {

        var listOfHasTag = getAllHasTags()

        isLoading.set(true)

        compositeDisposable.add(
            dataManager.saveVideo(
                SaveVideoRequest(
                    captionText.get()!!,
                    listOfHasTag,
                    false,
                    title.get()!!,
                    videoURL,
                    visibility
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::error)
        )
    }

    fun success(any: Any) {
        isLoading.set(false)
        getNavigator()?.onError(Throwable("your video has been post successfully"))
        getNavigator()!!.onBackPress()
    }

    fun error(throwable: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(Throwable(throwable))
    }

    private fun getAllHasTags(): ArrayList<String> {

        var array = arrayListOf<String>()
        val regexPattern = "(#\\w+)"

        var p = Pattern.compile(regexPattern);
        var m = p.matcher(captionText.get());
        while (m.find()) {
            array.add(m.group(1))
        }

        return array
    }


    fun validate(): Boolean {
        return when {
            title.get()!!.isEmpty() -> {
                titleError.set(
                    dataManager.getResourceProvider().getString(R.string.title_can_not_be_empty)
                )
                false
            }
            captionText.get()!!.isEmpty() -> {
                captionError.set(
                    dataManager.getResourceProvider().getString(R.string.caption_can_not_be_empty)
                )
                false
            }

            visibility.isEmpty() -> {

                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider()
                            .getString(R.string.please_select_visibility_type)
                    ), false
                )

                false
            }

            else -> {
                true
            }

        }
    }

    fun onTitleChange(s: CharSequence, start: Int, before: Int, count: Int) {
        titleError.set("")
    }

    fun onCaptionChange(s: CharSequence, start: Int, before: Int, count: Int) {
        captionError.set("")
    }
}