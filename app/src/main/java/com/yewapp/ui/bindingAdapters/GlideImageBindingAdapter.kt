package com.yewapp.ui.bindingAdapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.yewapp.R

class GlideImageBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("app:setListImageRes")
        fun setListUserImage(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                //  .thumbnail(Glide.with(imgView.context).load(R.raw.loading))
                .placeholder(ContextCompat.getDrawable(imgView.context, R.drawable.bigloader))
                .error(ContextCompat.getDrawable(imgView.context, R.drawable.bigloader))
                .into(imgView)

        }

        @JvmStatic
        @BindingAdapter("app:setImageRes")
        fun setUserImage(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                // .thumbnail(Glide.with(imgView.context).load(R.drawable.loading))
                .placeholder(
                    ContextCompat.getDrawable(
                        imgView.context,
                        R.drawable.ic_list_placeholder
                    )
                )
//                .error(ContextCompat.getDrawable(imgView.context, R.drawable.loading))
                .into(imgView)
        }

        @JvmStatic
        @BindingAdapter("app:setChallengeImage")
        fun setChallengeImage(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                // .thumbnail(Glide.with(imgView.context).load(R.drawable.loading))
//                .placeholder(
//                    ContextCompat.getDrawable(
//                        imgView.context,
//                        R.drawable.ic_list_placeholder
//                    )
//                )
//                .error(ContextCompat.getDrawable(imgView.context, R.drawable.loading))
                .into(imgView)
        }


        @JvmStatic
        @BindingAdapter("app:setUserImage")
        fun setUserImageNew(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                .thumbnail(Glide.with(imgView.context).load(R.drawable.ic_person))
                .placeholder(
                    ContextCompat.getDrawable(
                        imgView.context,
                        R.drawable.ic_person
                    )
                )
                .error(ContextCompat.getDrawable(imgView.context, R.drawable.ic_person))
                .into(imgView)
        }


        @JvmStatic
        @BindingAdapter("app:setChallengeMemberImage")
        fun setChallengeMemberImage(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                .thumbnail(Glide.with(imgView.context).load(R.drawable.test_icon))
                .placeholder(
                    ContextCompat.getDrawable(
                        imgView.context,
                        R.drawable.test_icon
                    )
                )
                .error(ContextCompat.getDrawable(imgView.context, R.drawable.test_icon))
                .into(imgView)
        }


        @JvmStatic
        @BindingAdapter("app:setSportsIcon")
        fun setSportsIcon(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                // .thumbnail(Glide.with(imgView.context).load(R.drawable.loading))
                .placeholder(ContextCompat.getDrawable(imgView.context, R.drawable.ic_cycle))
                .error(ContextCompat.getDrawable(imgView.context, R.drawable.ic_cycle))
                .into(imgView)
        }


        @JvmStatic
        @BindingAdapter("app:setCustomDrawableStart")
        fun setCustomDrawableStart(textView: TextView, url: String?) {
            val url = url ?: ""
            Glide.with(textView.context).load("url").apply(RequestOptions().fitCenter()).into(
                object : CustomTarget<Drawable>(20, 20) {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
                    ) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
//                        textView.setCompoundDrawablesWithIntrinsicBounds(placeholder, null, null, null)
                    }
                }
            )


//            Glide
//                .with(imgView)
//                .load(Uri.parse(url))
//                // .thumbnail(Glide.with(imgView.context).load(R.drawable.loading))
//                .placeholder(ContextCompat.getDrawable(imgView.context, R.drawable.ic_app_placeholder))
//                .error(ContextCompat.getDrawable(imgView.context,R.drawable.loading))
//                .into(imgView)
        }


        @JvmStatic
        @BindingAdapter("app:setImageUpload")
        fun setImageUpload(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                // .thumbnail(Glide.with(imgView.context).load(R.drawable.loading))
                // .placeholder(ContextCompat.getDrawable(imgView.context, R.drawable.loading))
                //.error(ContextCompat.getDrawable(imgView.context,R.drawable.loading))
                .into(imgView)

        }

        @JvmStatic
        @BindingAdapter("app:setImage")
        fun setImage(imgView: ImageView, url: String?) {
            val url = url ?: ""
            Glide
                .with(imgView)
                .load(Uri.parse(url))
                // .thumbnail(Glide.with(imgView.context).load(R.drawable.loading))
                //.placeholder(ContextCompat.getDrawable(imgView.context, R.drawable.ic_app_logo))
                .error(ContextCompat.getDrawable(imgView.context, R.drawable.ic_app_logo))
                .into(imgView)
        }

        @JvmStatic
        @BindingAdapter("app:setImageBitmap")
        fun setImageBitmap(imgView: ImageView, bitmap: Bitmap?) {
            Glide
                .with(imgView)
                .load(bitmap)
                .placeholder(ContextCompat.getDrawable(imgView.context, R.drawable.ic_app_logo))
                .error(ContextCompat.getDrawable(imgView.context, R.drawable.ic_app_logo))
                .into(imgView)


        }

        @JvmStatic
        @BindingAdapter("app:setImageDrawable")
        fun setImageDrawable(imgView: ImageView, image: Drawable?) {
            Glide
                .with(imgView)
                .load(image)
                // .thumbnail(Glide.with(imgView.context).load(R.drawable.loading))
                //.placeholder(ContextCompat.getDrawable(imgView.context, R.drawable.ic_app_logo))
                .error(ContextCompat.getDrawable(imgView.context, R.drawable.ic_app_logo))
                .into(imgView)
        }

    }
}