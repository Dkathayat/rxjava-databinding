package com.yewapp.utils.network

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val KEY = ""
//BuildConfig.PRIVATE_KEY

var map: MutableMap<String, Any> = mutableMapOf()

fun String.encrypt(): String {
    val key =
        getKey(KEY)
    val ivString = getAlphaNumericString(16)
    val ivByteArray = ivString.toByteArray()
    val iv = IvParameterSpec(ivByteArray)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)

    val encrypted = Base64.encode(cipher.doFinal(this.toByteArray()), Base64.NO_PADDING)

    map["iv"] = ivByteArray
    map["encrypted"] = ivString + String(encrypted)
    map["key"] = key

    return ivString + String(encrypted)
}

fun String.decrypt(): String {
    val key = getKey(KEY)
    val ivByteArray = getIV(this)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ivByteArray))

    val inputMessage = this
    val extractedString = this.substring(16 until this.length)
    val decodedMessage = Base64.decode(extractedString, Base64.NO_PADDING)
    val decrypted = cipher.doFinal(decodedMessage)

    return String(decrypted)
}

fun getKey(string: String): SecretKeySpec {

    return SecretKeySpec(string.toByteArray(), "AES_256") // 4
}

fun getIV(string: String): ByteArray {
    val iv = string.substring(0 until 16)
    return iv.toByteArray()
}

fun getAlphaNumericString(n: Int): String {
    val alphaNumericString = (
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz")

    val sb = StringBuilder(n)
    for (i in 0 until n) {
        val index = (alphaNumericString.length * Math.random()).toInt()
        sb.append(alphaNumericString[index])
    }
    return sb.toString()
}