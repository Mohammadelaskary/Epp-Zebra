package net.gbs.epp_project.Ui.Audit.StartAudit.AuditDataDialog.AuditLocatorsDialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.AuditLocator
import net.gbs.epp_project.R
import net.gbs.epp_project.databinding.AuditLocatorItemBinding

class AuditLocatorsAdapter(private val context: Context):Adapter<AuditLocatorsAdapter.AuditLocatorsViewHolder>() {
    var auditLocator:AuditLocator? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class AuditLocatorsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AuditLocatorItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditLocatorsViewHolder {
        val binding= AuditLocatorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AuditLocatorsViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return auditLocator?.auditOrderList?.size!!
    }

    override fun onBindViewHolder(holder: AuditLocatorsViewHolder, position: Int) {
        holder.binding.locatorCode.text = auditLocator!!.auditOrderList[position].locatorCode
        if (auditLocator!!.isFullScanned){
            holder.binding.locatorCode.setTextColor(context.getColor(R.color.green))
        } else {
            holder.binding.locatorCode.setTextColor(context.getColor(R.color.black))
        }
    }
}