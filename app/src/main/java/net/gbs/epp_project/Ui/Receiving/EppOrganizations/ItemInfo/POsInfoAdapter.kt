package net.gbs.epp_project.Ui.Receiving.EppOrganizations.ItemInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.databinding.ReceivingItemLayoutBinding

class POsInfoAdapter: Adapter<POsInfoAdapter.ItemInfoViewHolder>() {
    var poList:List<PODetailsItem2> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ItemInfoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ReceivingItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemInfoViewHolder {
        val binding = ReceivingItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemInfoViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return poList.size
    }

    override fun onBindViewHolder(holder: ItemInfoViewHolder, position: Int) {
        val poDetailsItem = poList[position]
        with(holder.binding){
            poNumber.text = poDetailsItem.pono
            date.text = poDetailsItem.receiptdate?.substring(0,10)
            itemCode.text = poDetailsItem.itemcode
            itemDesc.text = poDetailsItem.itemdesc
            orgCode.text = poDetailsItem.organizationCode
            qty.text = poDetailsItem.poLineQty.toString()
            val qtyToBeReceived = poDetailsItem.poLineQty!! - poDetailsItem.itemqtyReceived!!
            quantityToBeReceived.text = qtyToBeReceived.toString()
        }
    }
}