package net.gbs.epp_project.Ui.Return.ReturnToVendor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.LotQty
import net.gbs.epp_project.Model.POLineReturn
import net.gbs.epp_project.databinding.PoLineLayoutBinding
import net.gbs.epp_project.databinding.ReturnToVendorItemsDialogItemLayoutBinding
import java.util.ArrayList

class PoLineAdapter(val poLines:List<POLineReturn>,val onDeleteButtonClicked: OnDeleteButtonClicked):Adapter<PoLineAdapter.PoLineViewHolder>(){

    inner class PoLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PoLineLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoLineViewHolder {
        val binding = PoLineLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PoLineViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return poLines.size
    }

    override fun onBindViewHolder(holder: PoLineViewHolder, position: Int) {
        val poLine = poLines[position]
        with(holder.binding){
            itemDesc.text = poLine.itemDescription
            receiptNo.text = poLine.receiptNum
            if (poLine.lots?.isNotEmpty()!!)
                lotQty.text = lotsToText(poLine.lots!!)
            else
                lotQty.text = poLine.quantityReturned.toString()
        }
        holder.binding.remove.setOnClickListener {
            onDeleteButtonClicked.onDeleteButtonClicked(position)
        }
    }

    private fun lotsToText(lots: ArrayList<LotQty>): String {
        var lotsText = ""
        lots.forEachIndexed { index, lotQty ->
            if (index == 0)
                lotsText+=lotQty
            else
                lotsText+=", $lotQty"
        }
        return lotsText
    }

    interface OnDeleteButtonClicked {
        fun onDeleteButtonClicked(position: Int)
    }
}