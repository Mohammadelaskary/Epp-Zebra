package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.StartReceiving

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.gbs.epp_project.Model.PODetailsItem
import net.gbs.epp_project.R
import net.gbs.epp_project.databinding.ReceivedPoLineLayoutBinding

class ReceivedPOItemAdapter(private val linesList:List<PODetailsItem>, private val onPOLineItemRemoved: OnPOLineItemRemoved) :
    RecyclerView.Adapter<ReceivedPOItemAdapter.ReceivedPOItemViewHolder>() {
    inner class ReceivedPOItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val binding = ReceivedPoLineLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedPOItemViewHolder {
        val binding = ReceivedPoLineLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReceivedPOItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return linesList.size
    }

    override fun onBindViewHolder(holder: ReceivedPOItemViewHolder, position: Int) {
        val poDetailsItem = linesList[position]
        with(holder){
            binding.itemCode.text = poDetailsItem.itemcode
            binding.itemDescription.text= poDetailsItem.itemDescription
            val receivedPerTotalPOQty = "${poDetailsItem.itemQtyReceived} / ${poDetailsItem.itemQty}"
            binding.receivedQtyPerTotalQty.text = receivedPerTotalPOQty
            binding.currentReceivedQty.text = poDetailsItem.currentReceivedQty.toString()
            binding.remove.setOnClickListener { onPOLineItemRemoved.onPOLineItemRemoved(position) }
        }
    }

    interface OnPOLineItemRemoved {
        fun onPOLineItemRemoved(position: Int)
    }
}