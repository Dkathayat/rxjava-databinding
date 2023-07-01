package com.yewapp.ui.modules.player

import android.content.Context
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.yewapp.R
import com.yewapp.databinding.ActivityPlayerVideoBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.S3Upload
import com.yewapp.ui.dialogs.challengepopupdialogs.ChallengeSingleSelectionPopUp
import com.yewapp.ui.modules.player.navigator.PlayerVideoNavigator
import com.yewapp.ui.modules.player.vm.PlayerVideoViewModel
import com.yewapp.utils.VIDEO
import java.io.File
import java.util.*


class PlayerVideoActivity :
    BaseActivity<PlayerVideoNavigator, PlayerVideoViewModel, ActivityPlayerVideoBinding>(),
    PlayerVideoNavigator {

    var videoURL =
        ""

    override fun getLayout(): Int {
        return R.layout.activity_player_video
    }

    override fun init() {
        bind(PlayerVideoViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: PlayerVideoViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityPlayerVideoBinding) {

        var intetn = intent


        if (intetn != null && intetn.extras != null && intetn.extras!!.containsKey("url")) {
            videoURL = intetn.extras!!.getString("url")!!
        }

        videoURL = getRealPathFromURI(this, Uri.parse(videoURL))!!
        setPlayer(videoURL)
        removeTheBottomActionButtons()
    }


    private fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val columnIndex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

    private fun removeTheBottomActionButtons() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
        }
    }

    override fun onChallengeAreaClick(view: View) {
        // showChallengeAreaPopUp()
        ChallengeSingleSelectionPopUp.Builder()
            .addList(viewModel.locationRadiusArray.toList())
            .setListener {
                viewModel.selectedChallengeArea.set(it)

            }
            .build()
            .show(view)
    }

    //creating a variable for exoplayer
    var exoPlayer: SimpleExoPlayer? = null


    private fun setPlayer(videoURL: String) {
        try {
            //bandwisthmeter is used for getting default bandwidth
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            // track selector is used to navigate between video using a default seekbar.
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            //we are ading our track selector to exoplayer.
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
            //exoPlayer= ExoPlayerFactory.newSimpleInstance(this,trackSelector);
            // we are parsing a video url and parsing its video uri.
            //  val videouri = Uri.parse(videoURL)
            // we are creating a variable for datasource factory and setting its user agent as 'exoplayer_view'
            val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")
            // we are creating a variable for extractor factory and setting it to default extractor factory.
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
            //we are creating a media source with above variables and passing our event handler as null,
            //  val mediaSource: MediaSource =
            //     ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null)
            //inside our exoplayer view we are setting our player
            viewDataBinding.idExoPlayerVIew.player = exoPlayer


            //we are preparing our exoplayer with media source.

            /* val filePath: String =
                 Environment.getExternalStorageDirectory().toString() + File.separator.toString() +
                         "video" + File.separator.toString() + "video1.mp4"*/
            Log.e("filepath", videoURL)
            val uri = Uri.parse(videoURL)

            val audioSource = ExtractorMediaSource(
                uri,
                DefaultDataSourceFactory(this, "MyExoplayer"),
                DefaultExtractorsFactory(),
                null,
                null
            )
            exoPlayer!!.prepare(audioSource)
            //we are setting our exoplayer when it is ready.
            exoPlayer!!.playWhenReady = true
            viewDataBinding.idExoPlayerVIew.hideController()
            viewDataBinding.idExoPlayerVIew.useController = false
            exoPlayer!!.addListener(object : ExoPlayer.EventListener {
                override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {

                }

                override fun onTracksChanged(
                    trackGroups: TrackGroupArray?,
                    trackSelections: TrackSelectionArray?
                ) {

                }

                override fun onLoadingChanged(isLoading: Boolean) {

                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_IDLE -> {}
                        ExoPlayer.STATE_BUFFERING -> {
                            Log.d("time--", (exoPlayer!!.duration / 1000).toString())
                        }
                        ExoPlayer.STATE_READY -> {

                            startTimer()

                        }
                        ExoPlayer.STATE_ENDED -> {
                            exoPlayer!!.seekTo(0)
                            exoPlayer!!.playWhenReady = true
                        }
                    }
                }

                override fun onPlayerError(error: ExoPlaybackException?) {

                }

                override fun onPositionDiscontinuity() {

                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

                }

            })


        } catch (e: Exception) {
            // below line is used for handling our errors.
            Log.e("TAG", "Error : $e")
        }
    }


    var timer = Timer()
    var timerTask: TimerTask? = null


    private fun startTimer() {

        timerTask = object : TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun run() {
                //Log.d("time--", (exoPlayer!!.duration/1000).toString())

                // Log.d("time--", ((exoPlayer!!.getCurrentPosition()*100)/exoPlayer!!.getDuration()).toString())

                //  Log.d("time--", ((exoPlayer!!.getCurrentPosition()/1000).toString()))
                runOnUiThread {

                    viewDataBinding.progressM.progress =
                        ((exoPlayer!!.currentPosition * 100) / exoPlayer!!.duration).toInt()
                    viewDataBinding.progressM.progressTintList =
                        ColorStateList.valueOf(Color.YELLOW)
                    viewDataBinding.tvTime.text =
                        getForamtedTime(exoPlayer!!.currentPosition / 1000)
                }
            }
        }
        timer.schedule(timerTask, 1000, 800)
    }

    private fun getForamtedTime(time: Long): String {
        val second = time % 60
        val minute = time / 60
        val hours = minute / 60

        var ss = ""
        var mm = ""
        var hh = ""
        ss = if (second.toString().length == 1) {
            "0$second"
        } else {
            second.toString()
        }
        mm = if (minute.toString().length == 1) {
            "0$minute"
        } else {
            minute.toString()
        }
        hh = if (hours.toString().length == 1) {
            "0$hours"
        } else {
            hours.toString()
        }

        return "$hh:$mm:$ss"
    }

    override fun pause() {
        pausePlayer()
    }

    override fun play() {
        startPlayer()
    }

    private fun pausePlayer() {
        exoPlayer!!.playWhenReady = false
        exoPlayer!!.playbackState
        viewDataBinding.bntPlay.setImageResource(R.drawable.ic_play_video)
    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun startPlayer() {
        exoPlayer!!.playWhenReady = true
        exoPlayer!!.playbackState
        viewDataBinding.bntPlay.setImageResource(R.drawable.ic_small_pause)
    }


    override fun navigateToUplish() {
        viewModel.isLoading.set(true)
        S3Upload.uploadVideo(VIDEO, this, Uri.fromFile(File(videoURL)),viewModel.awsCredentials, object :
            S3Upload.OnS3UploadListener {
            override fun onS3UploadClicked(url: String) {

            }

            override fun onS3UploadVideoClicked(url: String, type: String) {
                viewModel.callPublishAPI(url)
            }

        })
    }
}