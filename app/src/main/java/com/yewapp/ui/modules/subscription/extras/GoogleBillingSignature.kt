package com.yewapp.ui.modules.subscription.extras

import android.text.TextUtils
import android.util.Base64
import java.io.IOException
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec

class GoogleBillingSignature {
    private val KEY_FACTORY_ALGORITM = "RSA"
    private val SIGNATURE_ALGORITM = "SHA1withRSA"

    fun verifyPurchase(base64PublicKey: String?, signedData: String, signature: String?): Boolean {
        if (TextUtils.isEmpty(signedData) || TextUtils.isEmpty(base64PublicKey) || TextUtils.isEmpty(
                signature
            )
        ) {
            return false
        }
        val key = generatePublicKey(base64PublicKey)
        return verifyData(key, signedData, signature)
    }

    private fun verifyData(key: PublicKey, signedData: String, signature: String?): Boolean {
        val signatureBytes: ByteArray = try {
            Base64.decode(signature,Base64.DEFAULT)
        } catch (e: java.lang.IllegalArgumentException){
            return false
        }
        try {
            val signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITM)
            signatureAlgorithm.initVerify(key)
            signatureAlgorithm.update(signedData.toByteArray())
            return signatureAlgorithm.verify(signatureBytes)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException){

        } catch (e : SignatureException){

        }
        return false
    }

    private fun generatePublicKey(encodedPublicKey: String?): PublicKey {

        return try {
            val decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITM)
            keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            throw IOException("Invalid Key $e")
        }
    }
}