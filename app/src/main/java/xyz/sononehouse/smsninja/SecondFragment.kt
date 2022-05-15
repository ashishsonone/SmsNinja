package xyz.sononehouse.smsninja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import xyz.sononehouse.smsninja.databinding.FragmentSecondBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), CoroutineScope by MainScope() {

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
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.decodeButton.setOnClickListener {

            launch {
                decodeAndShow()
            }

        }

        binding.secretET.setText(QuickStore(requireActivity()).get("secretKey")!!)
        binding.locationKeyET.setText(QuickStore(requireActivity()).get("locationKey")!!)
    }

    suspend fun decodeAndShow() {
        try {
            val locationKey = binding.locationKeyET.text.toString()
            val secret = binding.secretET.text.toString()

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