package xyz.sononehouse.smsninja

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import xyz.sononehouse.smsninja.databinding.FragmentRulesBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RulesListPage : Fragment(), CoroutineScope by MainScope(){
    val LOGTAG = "``FirstFragment"

    private var _binding: FragmentRulesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRulesBinding.inflate(inflater, container, false)
        populateData()
        return binding.root
    }

    private fun populateData() {
        val dataModelList: MutableList<TestDataModel> = ArrayList<TestDataModel>()
        dataModelList.add(TestDataModel("Android Oreo", "8.1"))
        dataModelList.add(TestDataModel("Android Pie", "9.0"))
        dataModelList.add(TestDataModel("Android Nougat", "7.0"))
        dataModelList.add(TestDataModel("Android Marshmallow", "6.0"))
        val myRecyclerViewAdapter = TestDataAdapter(dataModelList, requireActivity())
        binding.myAdapter = myRecyclerViewAdapter

//        binding.recyclerView.adapter = myRecyclerViewAdapter

        Log.d(LOGTAG, "populateComplete")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}