package com.yewapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.IOException
import java.net.URL

class BitmapImageRotator {
    companion object {
        suspend fun rotateImageBySize(imageUrl: String): Bitmap? {
            try {
                val url = URL(imageUrl)
                val bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                val matrix = Matrix()
                matrix.postRotate(90.0f)

                val scaledBitmap =
                    Bitmap.createScaledBitmap(
                        bitmapImage,
                        bitmapImage.width,
                        bitmapImage.height,
                        true
                    )

                val rotatedBitmap = Bitmap.createBitmap(
                    scaledBitmap,
                    0,
                    0,
                    scaledBitmap.width,
                    scaledBitmap.height,
                    matrix,
                    true
                )
                return rotatedBitmap
            } catch (e: IOException) {
                println(e)
            }
            return null
        }
    }
}