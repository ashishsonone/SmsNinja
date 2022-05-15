package xyz.sononehouse.smsninja

// Java program to generate
// a symmetric key
import android.util.Base64
import android.util.Log
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// Class to create a
// symmetric key
object EncryptionUtils {
    private const val AES = "AES"
    // We are using a Block cipher(CBC mode)
    private const val AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"
    private const val LOGTAG = "``EncryptionUtils"

    // Function to create a secret key
    @Throws(Exception::class)
    fun createAESKey(): SecretKey {

        // Creating a new instance of
        // SecureRandom class.
        val securerandom = SecureRandom()

        // Passing the string to
        // KeyGenerator
        val keygenerator =
            KeyGenerator.getInstance(AES)

        // Initializing the KeyGenerator
        // with 256 bits.
        keygenerator.init(128, securerandom)
        return keygenerator.generateKey()
    }

    // Function to initialize a vector
    // with an arbitrary value
    fun createInitializationVector(): ByteArray? {

        // Used with encryption
        val initializationVector = ByteArray(16)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(initializationVector)
        return initializationVector
    }

    // This function takes plaintext,
    // the key with an initialization
    // vector to convert plainText
    // into CipherText.
    @Throws(java.lang.Exception::class)
    fun do_AESEncryption(
        plainText: String,
        secretKey: SecretKey?,
        initializationVector: ByteArray?
    ): ByteArray? {
        val cipher: Cipher = Cipher.getInstance(
            AES_CIPHER_ALGORITHM
        )
        val ivParameterSpec = IvParameterSpec(
            initializationVector
        )
        cipher.init(
            Cipher.ENCRYPT_MODE,
            secretKey,
            ivParameterSpec
        )
        return cipher.doFinal(
            plainText.toByteArray()
        )
    }

    // This function performs the
    // reverse operation of the
    // do_AESEncryption function.
    // It converts ciphertext to
    // the plaintext using the key.
    @Throws(java.lang.Exception::class)
    fun do_AESDecryption(
        cipherText: ByteArray?,
        secretKey: SecretKey?,
        initializationVector: ByteArray?
    ): String? {
        val cipher: Cipher = Cipher.getInstance(
            AES_CIPHER_ALGORITHM
        )
        val ivParameterSpec = IvParameterSpec(
            initializationVector
        )
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            ivParameterSpec
        )
        val result: ByteArray = cipher.doFinal(cipherText)

        return String(result)
    }


    fun encodeBase64(bytes: ByteArray ): ByteArray {
        return Base64.encode(bytes, Base64.DEFAULT)
    }

    fun decodeBase64(input: String): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }

    // Driver code
    fun test() {
        try {
            val symmetricKey = createAESKey()
            val encodedSecret =  String(encodeBase64(symmetricKey.encoded))
            Log.d(LOGTAG,  "The Symmetric Key is :" + encodedSecret)

            val initializationVector = createInitializationVector()
            val encodedIV = String(encodeBase64(initializationVector!!))
            Log.d(LOGTAG,  "IV is :" + encodedIV)

            val plainText = ("Hello World !. This is a message from ICICI Bank")

            val cipherText = do_AESEncryption(
                plainText,
                symmetricKey,
                initializationVector
            )

            val encodedCipherText = String(encodeBase64(cipherText!!))
            Log.d(LOGTAG, "Encrypted Message is: " + encodedCipherText)

            val encPayload = "${encodedIV}:${encodedCipherText}"
            Log.d(LOGTAG, "Encrypted Payload is: " + encPayload)

            val decryptedText = do_AESDecryption(
                cipherText,
                symmetricKey,
                initializationVector
            )

            Log.d(LOGTAG, "Your original message is: " + decryptedText);
        }
        catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getSecretKey(decodedKey: ByteArray): SecretKey {
        val originalKey: SecretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        return originalKey
    }

    fun genBase64EncryptedPayload(plainText: String, base64SecretKey: String): String {
        val k : SecretKey = getSecretKey(decodeBase64(base64SecretKey))

        val initializationVector = createInitializationVector()
        val encodedIV = String(encodeBase64(initializationVector!!))

        val cipherText = do_AESEncryption(
            plainText,
            k,
            initializationVector
        )
        val encodedCipherText = String(encodeBase64(cipherText!!))

        val encPayload = "${encodedIV}:${encodedCipherText}"
        Log.d(LOGTAG, "Encrypted Payload is: " + encPayload)

        return encPayload
    }
}