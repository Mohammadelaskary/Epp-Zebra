package net.gbs.epp_project.Ui.FinishedProductsItemInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.gbs.epp_project.Model.OnHandItemForAllocate
import net.gbs.epp_project.Model.OnHandItemLot
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.databinding.FinishedProductsInfoItemLayoutBinding
import net.gbs.epp_project.databinding.InfoItemLayoutBinding

class FinishedProductsItemInfoAdapter: RecyclerView.Adapter<FinishedProductsItemInfoAdapter.ItemInfoViewHolder>() {
    var itemList:List<OnHandItemLot> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ItemInfoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = FinishedProductsInfoItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemInfoViewHolder {
        val binding = FinishedProductsInfoItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemInfoViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemInfoViewHolder, position: Int) {
        val item = itemList[position]
        with(holder.binding){
            itemCode.text = item.itemCode
            itemDesc.text = item.itemDescription
            orgCode.text = item.orgCode
            onHandQty.text = item.onhand.toString()
            locatorCode.text = item.locator
            lotNum.text = item.lotNum
            availableQty.text = item.availbleQty.toString()
        }
    }
}