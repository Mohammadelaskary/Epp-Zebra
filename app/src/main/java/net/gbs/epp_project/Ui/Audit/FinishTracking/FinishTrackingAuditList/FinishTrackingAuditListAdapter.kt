package net.gbs.epp_project.Ui.Audit.FinishTracking.FinishTrackingAuditList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.AuditOrder
import net.gbs.epp_project.databinding.AuditOrderItemLayoutBinding
import net.gbs.epp_project.databinding.AuditedTransactionItemLayoutBinding

class FinishTrackingAuditListAdapter(private val onOrderItemClicked:OnFinishTrackingOrderItemClicked):
    Adapter<FinishTrackingAuditListAdapter.FinishTrackingAuditListViewHolder>() {
    var auditOrders: List<AuditOrder> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class FinishTrackingAuditListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AuditOrderItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishTrackingAuditListViewHolder {
        val binding= AuditOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FinishTrackingAuditListViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return auditOrders.size
    }

    override fun onBindViewHolder(holder: FinishTrackingAuditListViewHolder, position: Int) {
        val order = auditOrders[position]
        with(holder){
            binding.orderNo.text = order.orderNo
            binding.orderDate.text = order.orderStartDate?.substring(0,10)
            binding.sunInventories.text = order.subInventoriesString()
            itemView.setOnClickListener { onOrderItemClicked.OnItemClicked(position) }
        }
    }
    interface OnFinishTrackingOrderItemClicked {
        fun OnItemClicked(position: Int)
    }
}