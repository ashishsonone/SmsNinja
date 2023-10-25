package xyz.sononehouse.smsninja

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.sononehouse.smsninja.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RuleEditorPage : Fragment(), CoroutineScope by MainScope(){
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
            findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment)


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

        binding.deleteRuleButton.setOnClickListener {
            this.binding.debugTV.text = "Deleting"

            launch {
                try {
                    delay(1000)
                    QuickStore.deleteForwardRule()
                    binding.debugTV.text = "[Success] Rule deleted"
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(LOGTAG, "[Error] deleting forward rule" + e.message)
                    binding.debugTV.text = "[Error] delete error ${e.message}"
                }
            }
        }

        binding.configureRuleButton.setOnClickListener {
            this.binding.debugTV.text = "Configuring...."

            launch {
                try {
                    delay(1000)

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

                    binding.debugTV.text = "[Success] Configuration done"

                }
                catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(LOGTAG, "[Error] configuring forward rule" + e.message)
                    binding.debugTV.text = "[Error] Configuration error ${e.message}"
                }
            }
        }

        binding.secretCopyButton.setOnClickListener {
            Log.d(LOGTAG, "secret copy called")
            val comboKey = binding.secretTV.text.toString().trim()
            Utility.storeIntoClipboard(comboKey)
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

        val secretKey = rule?.base64SecretKey?: "NA"
        val locationKey = rule?.locationKey?: "NA"
        val comboKey = "$locationKey:$secretKey"
        binding.secretTV.setText(comboKey)
        binding.locationKeyTV.setText(locationKey)

        checkPermissions()
    }

    fun checkPermissions() {
        val requiredPermission = Manifest.permission.RECEIVE_SMS
        val checkVal = requireActivity().checkCallingOrSelfPermission(requiredPermission)

        val stats = QuickStore.getStats()
        val gson = Gson()
        val jsonValue = gson.toJson(stats)

        binding.debugTV.text = "Sms Permission Granted = ${checkVal == PackageManager.PERMISSION_GRANTED}\nstats=${jsonValue}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}