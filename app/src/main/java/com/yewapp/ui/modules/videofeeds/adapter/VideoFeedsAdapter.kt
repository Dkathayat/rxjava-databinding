package com.yewapp.ui.modules.videofeeds.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.databinding.ItemVideoFeedsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VideoFeedsAdapter(
    var userId: Int,
    var listData: ArrayList<VideoData>,
    // var listData: ArrayList<String>,
    val listener: ItemVideoFeedsViewModel.OnItemClickListener

) : BaseRecyclerAdapter<VideoFeedsAdapter.ChooseContactsViewHolder,
        //String
        VideoData
        >(listData) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ChooseContactsViewHolder = ChooseContactsViewHolder(
        ItemVideoFeedsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ChooseContactsViewHolder, position: Int) {

        holder.bind(
            ItemVideoFeedsViewModel(
                userId,
                listData[position],
                position,
                listener
            )
        )//,listData[position].files[position].url,holder.itemView.context
        //holder.setPlayer(listData.get(holder.adapterPosition),holder.itemView.context,holder.idExoPlayerVIew,holder.videoProgressBar)
        holder.setData(listData[position].files[0].url)
    }

    class ChooseContactsViewHolder(
        private val binding: ItemVideoFeedsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var videoView: VideoView
        lateinit var videoProgressBar: ProgressBar
        fun bind(viewModel: ItemVideoFeedsViewModel) {//,url:String,context: Context
            binding.viewModel = viewModel
            videoView = binding.idExoPlayerVIew
            videoProgressBar = binding.videoProgressBar
        }

        /*  var exoPlayer: SimpleExoPlayer? = null
          fun setPlayer(
              videoURL: String,
              context: Context,
              idExoPlayerVIew: SimpleExoPlayerView,
              videoProgressBar: ProgressBar
          ) {
              try {
                  //bandwisthmeter is used for getting default bandwidth
                  val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
                  // track selector is used to navigate between video using a default seekbar.
                  val trackSelector: TrackSelector =
                      DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
                  //we are ading our track selector to exoplayer.
                  exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
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
                  idExoPlayerVIew!!.setPlayer(exoPlayer)

                  //we are preparing our exoplayer with media source.

                  val uri = Uri.parse(videoURL)

                  val audioSource = ExtractorMediaSource(
                      uri,
                      DefaultDataSourceFactory(context, "MyExoplayer"),
                      DefaultExtractorsFactory(),
                      null,
                      null
                  )

                  exoPlayer!!.prepare(audioSource)
                  //we are setting our exoplayer when it is ready.
                  exoPlayer!!.setPlayWhenReady(true)
                  idExoPlayerVIew!!.hideController()
                  idExoPlayerVIew!!.setUseController(false)
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
                              ExoPlayer.STATE_IDLE -> {

                              }
                              ExoPlayer.STATE_BUFFERING -> {
                                  Log.d("time--", (exoPlayer!!.duration / 1000).toString())
                                  videoProgressBar.visibility=View.VISIBLE
                              }
                              ExoPlayer.STATE_READY -> {
                                  //  startTimer()
                                  videoProgressBar.visibility=View.GONE
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
          }*/

        fun setData(obj: String?) {
            CoroutineScope(Dispatchers.IO).launch {
            videoView.setVideoPath(obj)
                }

            videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener { mediaPlayer ->
                videoProgressBar.visibility = View.GONE
                mediaPlayer.start()
            })
            videoView.setOnCompletionListener(MediaPlayer.OnCompletionListener { mediaPlayer -> mediaPlayer.start() })
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /* override fun onViewAttachedToWindow(holder: ChooseContactsViewHolder) {
          super.onViewAttachedToWindow(holder)

          setPlayer(listData.get(holder.adapterPosition),holder.itemView.context,holder.idExoPlayerVIew)
      }*/

    /* override fun onViewDetachedFromWindow(holder: ChooseContactsViewHolder) {
         super.onViewDetachedFromWindow(holder)

         stopPlayer()
     }*/

    //creating a variable for exoplayer


    /* private fun stopPlayer() {
         exoPlayer!!.stop()
     }




     private fun startPlayer() {
         exoPlayer!!.playWhenReady = true
         exoPlayer!!.playbackState

     }*/
    fun clearItems() {
        listData.clear()
        notifyDataSetChanged()
    }

    fun updateLikedItem(videoData: VideoData, position: Int) {
        listData[position] = videoData
        notifyItemChanged(position)
    }

    fun updateFollowStatus(followStatus: Boolean, position: Int) {
        listData[position].createdBy.following = followStatus
        notifyItemChanged(position)
    }
}