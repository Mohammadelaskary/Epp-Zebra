package net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.MoveOrderLinesDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.MoveOrderLine
import net.gbs.epp_project.Model.ReturnWorkOrderLine
import net.gbs.epp_project.databinding.MoveOrderLineItemLayoutBinding

class ReturnWorkOrderLinesAdapter(val orderLineItemClicked: OnWorkOrderLineItemClicked):Adapter<ReturnWorkOrderLinesAdapter.MoveOrderLinesViewHolder>() {
    var linesList :List<ReturnWorkOrderLine> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MoveOrderLinesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = MoveOrderLineItemLayoutBinding.bind(itemView)
    }
    interface OnWorkOrderLineItemClicked {
        fun onWorkOrderLineClicked(item:ReturnWorkOrderLine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveOrderLinesViewHolder {
        val binding = MoveOrderLineItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MoveOrderLinesViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return linesList.size
    }

    override fun onBindViewHolder(holder: MoveOrderLinesViewHolder, position: Int) {
        val line = linesList[position]
        with(holder.binding){
            itemCode.text = line.inventorYITEMCODE
            itemDescription.text = line.inventorYITEMDESC
            qty.text = line.quantity.toString()
        }
        holder.itemView.setOnClickListener {
            orderLineItemClicked.onWorkOrderLineClicked(line)
        }
    }
}