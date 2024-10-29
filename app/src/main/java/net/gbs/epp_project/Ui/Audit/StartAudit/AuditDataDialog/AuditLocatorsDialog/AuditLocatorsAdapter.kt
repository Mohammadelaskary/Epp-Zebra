package net.gbs.epp_project.Ui.Audit.StartAudit.AuditDataDialog.AuditLocatorsDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.gbs.epp_project.Model.AuditOrderSubinventory
import net.gbs.epp_project.databinding.AuditLocatorItemBinding
import net.gbs.epp_project.databinding.AuditedTransactionItemLayoutBinding

class AuditLocatorsAdapter():Adapter<AuditLocatorsAdapter.AuditLocatorsViewHolder>() {
    var locatorsList:List<AuditOrderSubinventory> = listOf()
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
        return locatorsList.size
    }

    override fun onBindViewHolder(holder: AuditLocatorsViewHolder, position: Int) {
        holder.binding.locatorCode.text = locatorsList[position].locatorCode
    }
}