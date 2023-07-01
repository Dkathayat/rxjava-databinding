package com.yewapp.ui.common

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class S3Upload {
    /**
     * @description: This method is called when the image is selected by camera or gallery to update
     * profile image in to aws s3 bucket.After selecting image .
     * 1.Create an Amazon S3 bucket to store the picture.
     * 2.Create an Amazon S3 client to communicate with the service and passing it your AWS
     * credentials object, and then pass the S3 client to the transfer utility.
     * 3.Finally, put the image object into the Amazon S3bucket.
     * 4.Create an override content type to ensure that the "content" will be treated as an image by
     * the browser for image content type is "image/jpeg" and for video video/mp4.
     * 5.To upload a file to S3, call Upload on the Transfer Utility object, passing the following
     * parameters :file , S3 bucketName
     * @param:
     * @author: Krishna kumari
     */
    companion object {

        var s3Client: AmazonS3Client? = null

        //        var credentials: BasicAWSCredentials
//            BasicAWSCredentials("AKIAUJGT6HVJCB3ZYBDH", "9ZLWK2JKZHU3zdR6v9cTgECU2jRumDL7Qgjrkav9")//old
//            BasicAWSCredentials("AKIAUJGT6HVJKMXI6C6X", "lD1M7/oxDfKcP8aheXGlt3M3JT7ljj0NLx6U1FCV")//narbir
        var url: String = ""
        lateinit var s3Listener: OnS3UploadListener

        fun upload(
            context: Context,
            fileUri: Uri,
            credentials: BasicAWSCredentials,
            listener: OnS3UploadListener
        ) {

            try {

                s3Client = AmazonS3Client(credentials)
                s3Listener = listener

                if (fileUri != null) {
                    val fileName = "${Date().time.toString()}_img.jpeg"
                    val file =
                        File(
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "/$fileName"
                        )
                    createFile(context, fileUri, file)
                    // Upload a image as a new object.
                    val request = PutObjectRequest(
                        "yew",
                        fileName,
                        file.absoluteFile
                    )
                    val metadata = ObjectMetadata()
                    metadata.contentType = "image/jpeg"
                    request.metadata = metadata
                    val transferUtility = TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().configuration)
                        .s3Client(s3Client)
                        .build()
                    val uploadObserver: TransferObserver = transferUtility.upload(
                        "yew",
                        fileName,
                        file?.absoluteFile!!,
                        metadata,
                        CannedAccessControlList.PublicRead
                    )

                    uploadObserver.setTransferListener(object : TransferListener {
                        override fun onStateChanged(id: Int, state: TransferState) {
                            if (TransferState.COMPLETED === state) {// "yew" + + "/"
                                url =
                                    "https://" + "yew.s3.amazonaws.com/" + uploadObserver.key // just like this https://s3.us-east-1.amazonaws.com/nmgtempbucket-us-east-1/FpsNL7lKNebL
                                //url =  "https://" + "s3.us-east-1.amazonaws.com/" + "yew" + "/" + uploadObserver.key
                                Log.d("SUCCESS", "UPLOAD DONE")
                                Log.d("SUCCESS", url)
                                listener.onS3UploadClicked(url)
                            } else if (TransferState.FAILED === state) {
                                // file.delete()
                                Log.d("FAIL", "UPLOAD FAIL")
                            }
                        }

                        override fun onProgressChanged(
                            id: Int,
                            bytesCurrent: Long,
                            bytesTotal: Long
                        ) {
                            val percent = (bytesCurrent.toFloat() / bytesTotal.toFloat()) * 100
                            val percentDone = percent.toInt()

                        }

                        override fun onError(id: Int, ex: Exception) {
                            ex.printStackTrace()
                        }
                    })
                }
            } catch (e: java.lang.Exception) {
                Log.i("S3Upload", "upload: $e")
            }

        }

        fun uploadVideo(
            type: String,
            context: Context,
            fileUri: Uri,
            credentials: BasicAWSCredentials,
            listener: OnS3UploadListener
        ) {

            try {

                s3Client = AmazonS3Client(credentials)
                s3Listener = listener

                if (fileUri != null) {
                    val fileName = "${Date().time.toString()}_mp4"
                    val file =
                        File(
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "/$fileName"
                        )
                    createFile(context, fileUri, file)
                    // Upload a image as a new object.
                    val request = PutObjectRequest(
                        "yew",
                        fileName,
                        file.absoluteFile
                    )
                    val metadata = ObjectMetadata()
                    metadata.contentType = "video/mp4"
                    request.metadata = metadata
                    val transferUtility = TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().configuration)
                        .s3Client(s3Client)
                        .build()
                    val uploadObserver: TransferObserver = transferUtility.upload(
                        "yew",
                        fileName,
                        file?.absoluteFile!!,
                        metadata,
                        CannedAccessControlList.PublicRead
                    )

                    uploadObserver.setTransferListener(object : TransferListener {
                        override fun onStateChanged(id: Int, state: TransferState) {
                            if (TransferState.COMPLETED === state) {// "yew" + + "/"
                                url =
                                    "https://" + "yew.s3.amazonaws.com/" + uploadObserver.key // just like this https://s3.us-east-1.amazonaws.com/nmgtempbucket-us-east-1/FpsNL7lKNebL
                                //url =  "https://" + "s3.us-east-1.amazonaws.com/" + "yew" + "/" + uploadObserver.key
                                Log.d("SUCCESS", "UPLOAD DONE")
                                listener.onS3UploadVideoClicked(url, type)
                            } else if (TransferState.FAILED === state) {
                                // file.delete()
                                Log.d("FAIL", "UPLOAD FAIL")
                            }
                        }

                        override fun onProgressChanged(
                            id: Int,
                            bytesCurrent: Long,
                            bytesTotal: Long
                        ) {
                            val percent = (bytesCurrent.toFloat() / bytesTotal.toFloat()) * 100
                            val percentDone = percent.toInt()
                            Log.i("S3Upload", "onProgressChanged: $percentDone")
                        }

                        override fun onError(id: Int, ex: Exception) {
                            ex.printStackTrace()
                        }
                    })
                }
            } catch (e: java.lang.Exception) {
                Log.i("S3Upload", "upload: $e")
            }

        }

        fun createFile(context: Context, srcUri: Uri, dstFile: File) {
            try {
                val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
                val outputStream = FileOutputStream(dstFile)
                IOUtils.copy(inputStream, outputStream)
                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    interface OnS3UploadListener {
        fun onS3UploadClicked(url: String)
        fun onS3UploadVideoClicked(url: String, type: String)
    }
}
