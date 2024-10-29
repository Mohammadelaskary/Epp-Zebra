package net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueReports.OnHandForIssueOrderReport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.gbs.epp_project.Model.OnHandIssueOrderItem
import net.gbs.epp_project.databinding.OnHandIssueOrderItemBinding

class OnHandIssueOrderReportAdapter():
    RecyclerView.Adapter<OnHandIssueOrderReportAdapter.OnHandIssueOrderReportViewHolder>() {
    inner class OnHandIssueOrderReportViewHolder (itemView: View) : ViewHolder(itemView){
        val binding = OnHandIssueOrderItemBinding.bind(itemView)
    }

    var items :List<OnHandIssueOrderItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnHandIssueOrderReportViewHolder {
        val binding = OnHandIssueOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OnHandIssueOrderReportViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OnHandIssueOrderReportViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding){
            itemCode.text = item.iteMCODE
            itemDesc.text = item.iteMDESCRIPTION
            qty.text = item.onhand.toString()
        }
    }
}