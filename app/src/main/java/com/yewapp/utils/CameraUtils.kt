package com.yewapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

fun getImageFile(data: ByteArray, activity: Activity): File {
    val file = File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PROFILE_PIC)
    val fileOutputStream = FileOutputStream(file)
    fileOutputStream.use { it.write(data) }

    val bitmap = BitmapFactory.decodeFile(file.path)
    val rotatedBitmap = rotateIfRequired(bitmap, file.path)
    val cropped = cropToCenter(rotatedBitmap)
    /*val compressed = compressImageSize(cropped, 1280)*/

    val finalFile = File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PROFILE_PIC)
    FileOutputStream(finalFile).use {
        cropped.compress(Bitmap.CompressFormat.JPEG, 80, it)
    }

    return file
}

private fun rotateIfRequired(bitmap: Bitmap, path: String): Bitmap {
    val ei = ExifInterface(path)

    return when (ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
        ExifInterface.ORIENTATION_NORMAL -> bitmap
        else -> bitmap
    }
}

private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height,
        matrix, true
    )
}

private fun cropToCenter(bitmap: Bitmap) =
    if (bitmap.width >= bitmap.height) {
        val x = (bitmap.width / 4 - bitmap.height / 4)
        Bitmap.createBitmap(bitmap, x, 0, bitmap.height, bitmap.height)
    } else {
        val y = (bitmap.height / 4 - bitmap.width / 4)
        Bitmap.createBitmap(bitmap, 0, y, bitmap.width, bitmap.width)
    }

fun decodeUri(context: Context, selectedImage: Uri?): Bitmap? {
    selectedImage ?: return null

    val o = BitmapFactory.Options()
    o.inJustDecodeBounds = true
    BitmapFactory.decodeStream(context.contentResolver.openInputStream(selectedImage), null, o)

    val requiredSize = 140

    var widthTmp = o.outWidth
    var heightTmp = o.outHeight
    var scale = 1
    while (true) {
        if (widthTmp / 2 < requiredSize || heightTmp / 2 < requiredSize) {
            break
        }
        widthTmp /= 2
        heightTmp /= 2
        scale *= 2
    }

    val o2 = BitmapFactory.Options()
    o2.inSampleSize = scale
    return BitmapFactory.decodeStream(
        context.contentResolver.openInputStream(selectedImage),
        null,
        o2
    )
}
