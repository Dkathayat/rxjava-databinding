package com.yewapp.ui.modules.managefeeds.extras

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yewapp.data.network.api.report.ReportedFeedFile
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.databinding.ReportedImageItemsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter


class ItemReportedFeedImageAdapter(
    val list: MutableList<ReportedFeedFile>,
    val reportedPosts: ReportedPosts
): BaseRecyclerAdapter<ItemReportedFeedImageAdapter.ReportedImageViewHolder, ReportedFeedFile>(list) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReportedImageViewHolder = ReportedImageViewHolder(
            ReportedImageItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ReportedImageViewHolder, position: Int) {
        holder.bind(
            ReportedImageViewModel(
            list[position],
            reportedPosts,
            position
        )
        )
    }

    class ReportedImageViewHolder(var binding: ReportedImageItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ReportedImageViewModel) {
            binding.viewModel = viewModel
            if (viewModel.items.type == "video") {
                binding.playButton.visibility = View.VISIBLE
                Glide.with(itemView).load(viewModel.items.url).into(binding.imagePostGlide)
                binding.imagePostGlide.visibility = View.VISIBLE
                binding.videoView.setVideoURI(Uri.parse(viewModel.items.url))
                binding.videoView.requestFocus()
                binding.videoView.setZOrderOnTop(true)
                binding.playButton.visibility = View.VISIBLE
            }

//            binding.playButton.setOnClickListener {
//                binding.videoView.setVideoURI(Uri.parse(viewModel.items.url))
//                binding.playButton.visibility = View.GONE
//                binding.progressbar.visibility = View.VISIBLE
//            }
            binding.videoView.setOnPreparedListener {
                it.start()
                binding.imagePostGlide.visibility = View.GONE
                binding.progressbar.visibility = View.GONE

            }
        }
    }

//    @Throws(Throwable::class)
//    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
//        var bitmap: Bitmap? = null
//        var mediaMetadataRetriever: MediaMetadataRetriever? = null
//        try {
//            mediaMetadataRetriever = MediaMetadataRetriever()
//            if (Build.VERSION.SDK_INT >= 14) mediaMetadataRetriever.setDataSource(
//                videoPath,
//                HashMap()
//            ) else mediaMetadataRetriever.setDataSource(videoPath)
//            //   mediaMetadataRetriever.setDataSource(videoPath);
//            bitmap =
//                mediaMetadataRetriever.getFrameAtTime(-1, MediaMetadataRetriever.OPTION_CLOSEST)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            throw Throwable(
//                "Exception in retriveVideoFrameFromVideo(String videoPath)"
//                        + e.message
//            )
//        } finally {
//            mediaMetadataRetriever?.release()
//        }
//        return bitmap
//    }
}