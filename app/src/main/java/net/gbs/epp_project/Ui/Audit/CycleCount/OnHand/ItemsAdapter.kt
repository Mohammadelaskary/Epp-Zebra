package net.gbs.epp_project.Ui.Audit.CycleCount.OnHand

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import net.gbs.epp_project.Model.ItemCompare
import net.gbs.epp_project.Model.OnHandItem
import net.gbs.epp_project.R
import net.gbs.epp_project.databinding.OnhandItemLayoutBinding

class ItemsAdapter(val context: Context) : Adapter<ItemsAdapter.ItemsViewHolder>() {
        var itemsList : List<ItemCompare> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

    inner class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = OnhandItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val binding = OnhandItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemsViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = itemsList[position]
        with(holder.binding){
            itemDescription.text = item.itemDescription
            currentLocatorCode.text = item.locatorCodeOnHand
            if ((item.onHandQty==item.countingQty)||(!item.isScanned!!)){
                qtyBecome.visibility = GONE
                countingQty.visibility = GONE
                onHandQty.text = item.onHandQty.toString()
            } else {
                qtyBecome.visibility = VISIBLE
                onHandQty.visibility = VISIBLE
                onHandQty.text = item.onHandQty.toString()
                currentLocatorCode.text = item.countingQty.toString()
            }

            if (item.isScanned!!){
                background.background = context.resources.getDrawable(R.color.green)
            }
        }
    }
}