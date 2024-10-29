package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.StartReceiving

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import net.gbs.epp_project.Model.PODetailsItem
import net.gbs.epp_project.R
import net.gbs.epp_project.databinding.ItemLayoutBinding

class PoDetailsAdapter(val context: Context,var onPOLineClicked:OnPOLineClicked) : RecyclerView.Adapter<PoDetailsAdapter.PoDetailsViewHolder>() {
    var poList: List<PODetailsItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class PoDetailsViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoDetailsViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PoDetailsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PoDetailsViewHolder, position: Int) {
        val item = poList[position]
        with(holder){
            if (item.isAdded)
                binding.root.setBackgroundColor(context.getColor(R.color.grey))
            else
                binding.root.setBackgroundColor(context.getColor(R.color.white))
            binding.itemCode.text = item.itemcode
            binding.itemDescription.text = item.itemDescription
            val receivedQtyPerTotalQty = "${item.itemQtyReceived} / ${item.itemQty}"
            binding.receivedQtyPerTotalQty.text = receivedQtyPerTotalQty
            itemView.setOnClickListener {
                if (!item.isAdded)
                    onPOLineClicked.onPOLineClicked(item)
                else
                    Toast.makeText(context,context.getString(R.string.added_before),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return poList.size
    }

    interface OnPOLineClicked{
        fun onPOLineClicked(po:PODetailsItem)
    }
}