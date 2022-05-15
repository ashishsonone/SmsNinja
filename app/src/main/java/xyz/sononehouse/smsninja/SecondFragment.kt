package xyz.sononehouse.smsninja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import xyz.sononehouse.smsninja.databinding.FragmentSecondBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

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

            try {
                val encryptedText = binding.encryptedET.text.toString()
                val secret = binding.secretET.text.toString()

                val tokens = encryptedText.split(':')
                val ivPart = tokens.get(0)
                val cipherPart = tokens.get(1)

                val decryptedText = EncryptionUtils.do_AESDecryption(
                    EncryptionUtils.decodeBase64(cipherPart),
                    EncryptionUtils.getSecretKey(EncryptionUtils.decodeBase64(secret)),
                    EncryptionUtils.decodeBase64(ivPart)
                )

                binding.decryptedTV.text = "[Success] ${decryptedText}"
            }
            catch (e : Exception) {
                binding.decryptedTV.text = "[Error] Unable to decode ${e.message}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}