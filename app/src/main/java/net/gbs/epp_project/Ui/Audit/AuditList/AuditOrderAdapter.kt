package net.gbs.epp_project.Ui.Audit.AuditList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.AuditOrder
import net.gbs.epp_project.databinding.AuditOrderItemLayoutBinding

class AuditOrderAdapter(val onAuditOrderItemClicked: OnAuditOrderItemClicked): Adapter<AuditOrderAdapter.AuditOrderViewHolder>() {
    var auditOrders:List<AuditOrder> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class AuditOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AuditOrderItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditOrderViewHolder {
        val binding = AuditOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AuditOrderViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return auditOrders.size
    }

    override fun onBindViewHolder(holder: AuditOrderViewHolder, position: Int) {
        val auditOrder = auditOrders[position]
        with(holder){
            binding.orderNo.text = auditOrder.orderDesc
            binding.orderDate.text = auditOrder.orderStartDate?.substring(0,10)
            binding.sunInventories.text = auditOrder.subInventoriesString()
            itemView.setOnClickListener { onAuditOrderItemClicked.OnOrderItemClicked(position) }
        }
    }

    interface OnAuditOrderItemClicked {
        fun OnOrderItemClicked(position: Int)
    }
}