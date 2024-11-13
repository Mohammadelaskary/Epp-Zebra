package net.gbs.epp_project.Ui.Audit.AuditedList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import net.gbs.epp_project.Model.AuditTransaction
import net.gbs.epp_project.databinding.AuditedTransactionItemLayoutBinding
import java.util.Locale

class AuditedListAdapter:Adapter<AuditedListAdapter.AuditedListViewHolder>(), Filterable {

    private var transactionsList: List<AuditTransaction> = listOf()
    var fullTransactionsList: List<AuditTransaction> = listOf()
        set(value) {
            field = value
            submitList(fullTransactionsList)
        }

    private fun submitList(list: List<AuditTransaction>) {
        transactionsList = list
        notifyDataSetChanged()
    }


    private val searchFilter :Filter = object :Filter(){
        override fun performFiltering(searchText: CharSequence): FilterResults {
            val filteredList = if (searchText.isEmpty()){
                fullTransactionsList
            } else {
                val lowerCaseSearchText = searchText.toString().lowercase(Locale.ROOT)
                fullTransactionsList.filter { it.itemCode?.lowercase(Locale.ROOT)?.contains(lowerCaseSearchText)!!
                        || it.locatorCode?.lowercase(Locale.ROOT)?.contains(lowerCaseSearchText)!!
                        || it.subInventoryCode?.lowercase(Locale.ROOT)?.contains(lowerCaseSearchText)!!
                        || it.itemDescription?.lowercase(Locale.ROOT)?.contains(lowerCaseSearchText)!!
                        || it.subInventoryDesc?.lowercase(Locale.ROOT)?.contains(lowerCaseSearchText)!!
                }
            }
            return FilterResults().apply { values = filteredList }
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults) {
            submitList(results.values as List<AuditTransaction>)
        }

    }
    inner class AuditedListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AuditedTransactionItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditedListViewHolder {
        val binding=AuditedTransactionItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AuditedListViewHolder(binding.root)
    }

    override fun getItemCount() = transactionsList.size

    override fun onBindViewHolder(holder: AuditedListViewHolder, position: Int) {
        val transaction = transactionsList[position]
        with(holder){
            binding.transactionDate.text = transaction.dateCount?.substring(0,10)
            binding.userName.text = transaction.userNameCount
            binding.itemCode.text = transaction.itemCode
            binding.itemDesc.text = transaction.itemDescription
            binding.orgName.text = transaction.orgName
            binding.subInventory.text = transaction.subInventoryCode
            binding.locatorCode.text = transaction.locatorCode
            binding.qty.text = transaction.qty.toString()
            binding.uom.text = transaction.uom
        }
    }

    override fun getFilter(): Filter {
        return searchFilter
    }
}