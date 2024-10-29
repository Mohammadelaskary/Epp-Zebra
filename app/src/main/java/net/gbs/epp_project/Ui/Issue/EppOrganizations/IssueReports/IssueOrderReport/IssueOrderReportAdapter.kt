package net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueReports.IssueOrderReport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.gbs.epp_project.Model.IssueOrderReportItem
import net.gbs.epp_project.databinding.IssueOrderItemBinding

class IssueOrderReportAdapter():
    RecyclerView.Adapter<IssueOrderReportAdapter.IssueReportViewHolder>() {
    inner class IssueReportViewHolder (itemView: View) : ViewHolder(itemView){
        val binding = IssueOrderItemBinding.bind(itemView)
    }

    var items :List<IssueOrderReportItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueReportViewHolder {
        val binding = IssueOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IssueReportViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: IssueReportViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding){
            itemCode.text = item.inventorYITEMCODE
            itemDesc.text = item.inventorYITEMDESC
            issuedQty.text = item.quantitYDELIVERED.toString()
            issueQty.text  = item.quantity.toString()
        }
    }
}