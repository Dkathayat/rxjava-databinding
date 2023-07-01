package com.yewapp.ui.modules.feed.vm

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.CreateFeedRequest
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.Files
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.EditFeedExtra.Companion.EDIT_FEED_DATA
import com.yewapp.ui.modules.feed.navigator.CreateFeedNavigator
import com.yewapp.utils.rx.SchedulerProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CreateFeedViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<CreateFeedNavigator>(dataManager, schedulerProvider) {

    var title = ObservableField<String>("")
    var description = ObservableField<String>("")
    var isDraft = false
    val titleError = ObservableField<String>("")
    val isPublishEnabled = ObservableBoolean(false)

    var editFeed: Feed? = null
    var _list = mutableListOf<String>()
    var list = MutableLiveData<List<String>>()
    val listLive: LiveData<List<String>> get() = list
    var _videoList = mutableListOf<String>()
    var videoList = MutableLiveData<List<String>>()
    val videoListLive: LiveData<List<String>> get() = videoList
    var videoImage1 = ObservableField<String>("")
    val videoImage1Status = ObservableBoolean(false)
    var videoImage2 = ObservableField<String>("")
    val videoImage2Status = ObservableBoolean(false)
    var videoImage3 = ObservableField<String>("")
    val videoImage3Status = ObservableBoolean(false)

    val isUploading = ObservableBoolean(false)

    var awsCredentials: BasicAWSCredentials

    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )

    }

    override fun setData(extras: Bundle?) {
        editFeed = extras?.getParcelable(EDIT_FEED_DATA)
        setEditedData()
    }

    fun onSaveDraftClick(view: View) {
        isDraft = true
        publishFeed()
    }

    fun onPublishClick(view: View) {
        isDraft = false
        publishFeed()
    }

    fun onBottomMenuClick(view: View) {
        when (view.id) {
            R.id.img_activity -> getNavigator()?.chooseActivity()
            R.id.img_location -> {
                //Fetch location here
            }
            R.id.img_photo -> {
                if (_list.size == 5) {
                    getNavigator()?.uploadValidation(
                        dataManager.getResourceProvider()
                            .getString(R.string.you_cannot_upload_morethan_five_images)
                    )
                } else {
                    getNavigator()?.launchImageOptions()
                }
            }
            R.id.img_video -> {
                if (_videoList.size == 3) {
                    getNavigator()?.uploadValidation(
                        dataManager.getResourceProvider()
                            .getString(R.string.you_cannot_upload_morethan_three_video)
                    )
                } else {
                    getNavigator()?.launchVideoPicker()
                }
            }
        }
    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.first_cross_icon -> {
                _videoList.removeAt(0)
                videoImage1Status.set(false)
            }
            R.id.second_cross_icon -> {
                _videoList.removeAt(1)
                videoImage2Status.set(false)
            }
            R.id.third_cross_icon -> {
                _videoList.removeAt(2)
                videoImage3Status.set(false)
            }
        }
    }

    // API Calls from here

    fun publishFeed() {
        if (!validateTitle()) {
            showTitleError()
            return
        }
        if (!validateDescription()) {
            showDescriptionError()
            return
        }

        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.createFeed(
                CreateFeedRequest(
                    title.get() ?: "",
                    description.get() ?: "",
                    isDraft,
                    "",
                    "",
                    "",
                    null,
                    mutableListOf(),
                    _list,
                    _videoList
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFeedSuccess, this::onFeedFailure)
        )
    }

    private fun onFeedSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(
            dataManager.getResourceProvider().getString(R.string.feed_save_successfully)
        )
        viewModelScope.launch {
            delay(1000)
            getNavigator()?.closeActivity()
        }
    }

    private fun onFeedFailure(t: Throwable) {
        isLoading.set(false)
    }

    fun validateTitle(): Boolean = when {
        title.get()!!.isEmpty() -> {
            false
        }
        else -> true
    }

    fun validateDescription() = when {
        description.get()!!.isEmpty() -> {
            false
        }
        else -> true
    }

    fun showTitleError() {
        when {
            title.get()!!.isEmpty() -> {
                titleError.set(getString(R.string.empty_title_error))
            }
        }
    }

    fun showDescriptionError() {
        when {
            description.get()!!.isEmpty() -> {
                getNavigator()?.onError(Throwable(getString(R.string.empty_description_error)))
            }
        }
    }

    fun onTitleFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateTitle())
            showTitleError()
    }

    fun onDescriptionFocusChanged(view: View, hasFocus: Boolean) {
        if (!hasFocus && !validateDescription())
            showDescriptionError()
    }


    fun onTitleChange(s: Editable) {
        titleError.set("")

        enablePublishButton()
    }

    fun onDescriptionChange(s: Editable) {
        enablePublishButton()
//        handleHashtags(s, start, before, count)
    }

    fun enablePublishButton() {
        if (validateDescription() && validateTitle())
            isPublishEnabled.set(true)
        else
            isPublishEnabled.set(false)
    }

    var spannableHastTags: Spannable = SpannableStringBuilder()

    private fun handleHashtags(s: CharSequence, start: Int, before: Int, count: Int) {
        var startChar: String? = null
        var isHashTag = false
        startChar = try {
            s[start].toString()
        } catch (e: Exception) {
            ""
        }

        if (startChar == "#") {
            changeTheColor(s.toString().substring(start), start, start)
            isHashTag = true
        }

        if (startChar == " ") {
            isHashTag = false
        }

        if (isHashTag) {
            changeTheColor(s.toString().substring(start), start, start)
            isHashTag = true
        }
    }


    private fun setEditedData() {

        title.set(editFeed?.title ?: "")
        description.set(editFeed?.description ?: "")


        editFeed?.files?.forEach { images ->
            if (!images.url.contains("mp4")) {
                _list.add(images.url)
                list.value = _list
            }
        }
        editFeed?.files?.forEach { videos ->
            if (videos.url.contains("mp4")) {
                _videoList.addAll(listOf(videos.url))
                videoList.value = _videoList

                if (_videoList.size == 1) {
                    videoImage1.set(videos.url)
                    videoImage1Status.set(true)
                }
                if (_videoList.size == 2) {
                    videoImage2.set(videos.url)
                    videoImage2Status.set(true)
                }
                if (_videoList.size == 3) {
                    videoImage3.set(videos.url)
                    videoImage3Status.set(true)
                }
            }
        }


    }

    private fun changeTheColor(s: String, start: Int, end: Int) {
        spannableHastTags.setSpan(
            ForegroundColorSpan(dataManager.getResourceProvider().getColor(R.color.colorPrimary)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}