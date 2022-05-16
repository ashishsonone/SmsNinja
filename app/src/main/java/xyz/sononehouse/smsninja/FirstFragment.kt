package xyz.sononehouse.smsninja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import xyz.sononehouse.smsninja.databinding.FragmentFirstBinding
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), CoroutineScope by MainScope(){

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
                if (QuickStore.get("locationKey").isNullOrEmpty()) {
                    val key = Coordinator().generateKey(QuickStore.get("clientId")!!)
                    QuickStore.set("locationKey", key)
                }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}