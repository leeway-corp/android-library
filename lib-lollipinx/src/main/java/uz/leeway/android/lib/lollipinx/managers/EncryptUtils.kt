package uz.leeway.android.lib.lollipinx.managers

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

object EncryptUtils {

    @JvmStatic
    fun encrypt(seed: String): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        secureRandom.setSeed(seed.toByteArray())

        keyGenerator.init(128, secureRandom)
        val skey = keyGenerator.generateKey()
        val rawKey: ByteArray = skey.encoded

        val skeySpec = SecretKeySpec(rawKey, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        val byteArray = cipher.doFinal(this.toByteArray())

        return byteArray.toString()
    }

    @JvmStatic
    fun decrypt(seed: String): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        secureRandom.setSeed(seed.toByteArray())

        keyGenerator.init(128, secureRandom)
        val skey = keyGenerator.generateKey()
        val rawKey: ByteArray = skey.encoded

        val skeySpec = SecretKeySpec(rawKey, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        val byteArray = cipher.doFinal(this.toByteArray())

        return byteArray.toString()
    }
}