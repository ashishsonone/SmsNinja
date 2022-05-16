package xyz.sononehouse.smsninja

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import xyz.sononehouse.smsninja.databinding.FragmentFirstBinding
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), CoroutineScope by MainScope(){
    val LOGTAG = "``FirstFragment"

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
//            val fr = ForwardRule(".*-ICICI", ".*", "xyz")
//            fr.isMatch("AM-ICICIB", "")
//            fr.isMatch("AM-CANBNK", "")
//            fr.isMatch("X-ICICIBX", "")
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)


            launch {
//                if (QuickStore.get("locationKey").isNullOrEmpty()) {
//                    val key = Coordinator().generateKey(QuickStore.get("clientId")!!)
//                    QuickStore.set("locationKey", key)
//                }

//                if (false ) {
//                    val rule = ForwardRule("test", ".*", "Sony LIV OTP", QuickStore.get("locationKey")!!, QuickStore.get("secretKey")!!)
//                    QuickStore.storeForwardRule(rule)
//                    Log.d(LOGTAG, "rule stored = ${QuickStore.getForwardRule()}")
//                }

                // val base64SecretKey = QuickStore.get("secretKey")

                // val payload = EncryptionUtils.genBase64EncryptedPayload("The time is " + Date(), base64SecretKey!!)
                // Coordinator().storeKV(QuickStore.get("locationKey")!!, payload, QuickStore.get("clientId")!!)
            }

//            val setCall = service.store("k1", "v1")
//            try {
//                val response = setCall.execute()
//                Log.d("``FirstFragment", "Result=${response.body()?.success}" + response.code())
//            }
//            catch (e: Exception) {
//                Log.d("``FirstFragment", "Error" + e.message)
//                e.printStackTrace()
//            }


        }

        binding.configureRuleButton.setOnClickListener {

            launch {
                try {

                    var rule = QuickStore.getForwardRule()
                    if (rule == null) {
                        val locationKey = Coordinator().generateKey(QuickStore.get("clientId")!!)

                        val symmetricKey = EncryptionUtils.createAESKey()
                        val secretKey =  String(EncryptionUtils.encodeBase64(symmetricKey.encoded))

                        rule = ForwardRule(
                            "test",
                            "",
                            "",
                            locationKey,
                            secretKey
                        )
                    }

                    rule.bodyPattern = binding.bodyRegexET.text.toString()
                    rule.senderPattern = binding.senderRegexET.text.toString()

                    QuickStore.storeForwardRule(rule)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(LOGTAG, "Error configuring forward rule")
                }
            }
        }

        binding.secretCopyButton.setOnClickListener {
            Log.d(LOGTAG, "secret copy called")
            Utility.storeIntoClipboard(binding.secretTV.text.toString())
        }

        binding.locationKeyCopyButton.setOnClickListener {
            Utility.storeIntoClipboard(binding.locationKeyTV.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()

        var rule = QuickStore.getForwardRule()
        binding.bodyRegexET.setText(rule?.bodyPattern?: "")
        binding.senderRegexET.setText(rule?.senderPattern?: "")
        binding.secretTV.setText(rule?.base64SecretKey?: "NA")
        binding.locationKeyTV.setText(rule?.locationKey?: "NA")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}