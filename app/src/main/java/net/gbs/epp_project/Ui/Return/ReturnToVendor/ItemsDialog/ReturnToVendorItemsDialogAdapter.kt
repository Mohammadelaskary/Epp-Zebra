package net.gbs.epp_project.Ui.Return.ReturnToVendor.ItemsDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.POItem
import net.gbs.epp_project.databinding.AuditedTransactionItemLayoutBinding
import net.gbs.epp_project.databinding.ReturnToVendorItemsDialogItemLayoutBinding

class ReturnToVendorItemsDialogAdapter(val onItemSelected: OnItemSelected):
    Adapter<ReturnToVendorItemsDialogAdapter.ReturnToVendorItemsDialogViewHolder>() {
        var itemsList = listOf<POItem>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }
    inner class ReturnToVendorItemsDialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ReturnToVendorItemsDialogItemLayoutBinding.bind(itemView)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReturnToVendorItemsDialogViewHolder {
        val binding=ReturnToVendorItemsDialogItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReturnToVendorItemsDialogViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: ReturnToVendorItemsDialogViewHolder, position: Int) {
        val item = itemsList[position]
        with(holder){
            with(binding){
                itemDesc.text = item.itemdesc
                receiptNo.text = item.receiptno
                qty.text = item.transQty.toString()
                status.text = item.parenTTRANSACTIONTYPE
            }
            itemView.setOnClickListener { onItemSelected.onItemSelected(item,position) }
        }
    }

    interface OnItemSelected {
        fun onItemSelected(item:POItem,position: Int)
    }
}