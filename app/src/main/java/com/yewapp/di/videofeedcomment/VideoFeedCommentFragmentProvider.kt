package com.yewapp.di.videofeedcomment

import com.yewapp.ui.modules.videofeedcomment.VideoFeedCommentBottomSheet
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class VideoFeedCommentFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideVideoFeedCommentBottomSheet(): VideoFeedCommentBottomSheet

}