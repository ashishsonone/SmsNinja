package xyz.sononehouse.smsninja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.sononehouse.smsninja.databinding.FragmentSecondBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DecoderPage : Fragment(), CoroutineScope by MainScope() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_Decoder_to_Rules)
        }

        binding.decodeButton.setOnClickListener {
            // persist any changes to locationKey and secretKey

            binding.decryptedTV.text = "Decoding...."

            val combo = binding.secretET.text.toString()
            val tokens = combo.split(":")
            if (tokens.size != 2) {
                binding.decryptedTV.text = "Invalid decoding key"
                return@setOnClickListener
            }

            val locationKey = tokens[0]
            val secretKey = tokens[1]


            QuickStore.set("locationKey", locationKey)
            QuickStore.set("secretKey", secretKey)

            launch {
                delay(1000)
                decodeAndShow(locationKey, secretKey)
            }

        }

//        binding.pasteLocationButton.setOnClickListener {
//            val clipValue = Utility.getClipboardValue()
//            if (clipValue != null) {
//                binding.locationKeyET.setText(clipValue)
//            }
//        }

        binding.pasteSecretButton.setOnClickListener {
            val clipValue = Utility.getClipboardValue()
            if (clipValue != null) {
                binding.secretET.setText(clipValue)
            }
        }
    }

    override fun onResume() {
        super.onResume()


        val secretKey = QuickStore.get("secretKey", "")
        val locationKey = QuickStore.get("locationKey", "")
        val combo = "$locationKey:$secretKey"

        binding.secretET.setText(combo)

        //binding.locationKeyET.setText(QuickStore.get("locationKey", ""))
    }

    suspend fun decodeAndShow(locationKey: String, secret: String) {
        try {
            val encryptedPayload = Coordinator().getK(locationKey)

            val tokens = encryptedPayload!!.split(':')
            val ivPart = tokens.get(0)
            val cipherPart = tokens.get(1)

            val decryptedText = EncryptionUtils.do_AESDecryption(
                EncryptionUtils.decodeBase64(cipherPart),
                EncryptionUtils.getSecretKey(EncryptionUtils.decodeBase64(secret)),
                EncryptionUtils.decodeBase64(ivPart)
            )

            binding.decryptedTV.text = "[Success]\n${decryptedText}"
        }
        catch (e : Exception) {
            binding.decryptedTV.text = "[Error]\nUnable to decode ${e.message}"
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}