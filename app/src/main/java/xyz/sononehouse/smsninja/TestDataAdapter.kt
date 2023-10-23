package xyz.sononehouse.smsninja

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.sononehouse.smsninja.databinding.XItemRowBinding

class TestDataAdapter(dataModelList: List<TestDataModel>, ctx: Context) :
    RecyclerView.Adapter<TestDataAdapter.ViewHolder>(), TestCustomClickListener {
    private val dataModelList: List<TestDataModel>
    private val context: Context

    init {
        this.dataModelList = dataModelList
        context = ctx
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: XItemRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.x_item_row, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel: TestDataModel = dataModelList[position]
        holder.bind(dataModel)
        holder.itemRowBinding.setItemClickListener(this)
    }

    override fun getItemCount(): Int {
        return dataModelList.size
    }

    inner class ViewHolder(itemRowBinding: XItemRowBinding) :
        RecyclerView.ViewHolder(itemRowBinding.getRoot()) {
        var itemRowBinding: XItemRowBinding

        init {
            this.itemRowBinding = itemRowBinding
        }

        fun bind(obj: Any?) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
        }
    }

    override fun cardClicked(f: TestDataModel?) {
        Toast.makeText(
            context, "You clicked " + f?.androidName,
            Toast.LENGTH_LONG
        ).show()
    }
}