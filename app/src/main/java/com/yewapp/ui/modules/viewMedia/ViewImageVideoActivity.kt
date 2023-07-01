package com.yewapp.ui.modules.viewMedia

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.yewapp.R
import com.yewapp.data.network.api.feed.Files
import com.yewapp.databinding.ViewFeedsMediaActivityBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.viewMedia.adapter.FeedsMediaAdapter
import com.yewapp.ui.modules.viewMedia.vm.ItemViewMediaViewModel
import com.yewapp.ui.modules.viewMedia.vm.ViewImageVideoViewModel

class ViewImageVideoActivity :
    BaseActivity<ViewImageVideoNavigator, ViewImageVideoViewModel, ViewFeedsMediaActivityBinding>(),
    ViewImageVideoNavigator {

    private lateinit var adapter: FeedsMediaAdapter
    var player: SimpleExoPlayer? = null


    override fun getLayout(): Int {
        return R.layout.view_feeds_media_activity
    }

    override fun init() {
        bind(ViewImageVideoViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ViewImageVideoViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ViewFeedsMediaActivityBinding) {

        val list = mutableListOf<Files>()

        adapter = FeedsMediaAdapter(list, object : ItemViewMediaViewModel.OnItemClickListener {
            override fun onClickItem(item: Files) {
                viewModel.image.set(item.url)
                if (item.type != "image") {
                    viewModel.videoIconVisibility.set(true)
                } else {
                    viewModel.videoIconVisibility.set(false)
                    onStopPlayer()
                }
            }
        })
        adapter.setHasStableIds(true)
        viewDataBinding.mediaRecycler!!.adapter = adapter
        viewDataBinding.mediaRecycler!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.imageListLive.observe(this, androidx.lifecycle.Observer {
            this.adapter.setItems(it)
        })
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    override fun onVideoClick(type: Boolean) {
        if (type) {

            viewModel.videoIconVisibility.set(false)
            viewModel.isPlayingVideo.set(true)
            initializePlayer()
        } else {
            viewModel.isPlayingVideo.set(false)
        }
    }

    override fun onStopPlayer() {
        viewModel.isPlayingVideo.set(false)
        player?.playWhenReady = false
        if (isFinishing) {
            releasePlayer()
        }
    }

    override fun crossClick() {
        finish()
    }

    /*  private fun buildMediaSource(uri:Uri): MediaSource? {
          val dataSourceFactory = DefaultDataSourceFactory(this, "sample")
          return ProgressiveMediaSource.Factory(dataSourceFactory)
              .createMediaSource(MediaItem.fromUri(uri))
      }*/

    private fun initializePlayer() {
        /*  player = SimpleExoPlayer.Builder(this).build()
          viewDataBinding.videoFullScreenPlayer1.player = player
          buildMediaSource(Uri.parse(viewModel.image.get().toString()))?.let {
              player?.prepare(it)
          }
          player?.playWhenReady = true*/

    }

//    override fun onResume() {
//        super.onResume()
//        player?.playWhenReady = true
//    }

    override fun onStop() {
        super.onStop()
        player?.playWhenReady = false
        if (isFinishing) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.release()
    }


}