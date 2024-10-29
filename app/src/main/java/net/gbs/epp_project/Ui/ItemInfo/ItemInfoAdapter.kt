package net.gbs.epp_project.Ui.ItemInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.gbs.epp_project.Model.OnHandItemForAllocate
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.databinding.InfoItemLayoutBinding

class ItemInfoAdapter: RecyclerView.Adapter<ItemInfoAdapter.ItemInfoViewHolder>() {
    var itemList:List<OnHandItemForAllocate> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ItemInfoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = InfoItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemInfoViewHolder {
        val binding = InfoItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemInfoViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemInfoViewHolder, position: Int) {
        val item = itemList[position]
        with(holder.binding){
            itemCode.text = item.iteMCODE
            itemDesc.text = item.iteMDESCRIPTION
            orgCode.text = item.orGCODE
            onHandQty.text = item.onhand.toString()
            locatorCode.text = item.locator
            availableQty.text = item.availblEQTY.toString()
        }
    }
}