package xyz.sononehouse.smsninja

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.sononehouse.smsninja.databinding.FragmentFirstBinding
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


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
            val fr = ForwardRule(".*-ICICI", ".*", "xyz")
            fr.isMatch("AM-ICICIB", "")
            fr.isMatch("AM-CANBNK", "")
            fr.isMatch("X-ICICIBX", "")
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

            launch {
                Coordinator().storeKV("ashish", "sonone")

                EncryptionUtils.test()
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