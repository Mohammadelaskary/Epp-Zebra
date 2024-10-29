package net.gbs.epp_project.Ui.Receiving.EppOrganizations.PutAway.Rejection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.Ui.Receiving.EppOrganizations.PutAway.Deliver.PurchaseOrdersPutAwayAdapter
import net.gbs.epp_project.databinding.PoItemPutAwayBinding


class PurchaseOrdersRejectionAdapter (private val putAwayItemClicked: PutAwayItemClick): RecyclerView.Adapter<PurchaseOrdersRejectionAdapter.PurchaseOrderPutAwayViewHolder>() {
    var poList: MutableList<PODetailsItem2> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PurchaseOrderPutAwayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PoItemPutAwayBinding.bind(view)
    }



    override fun onBindViewHolder(holder: PurchaseOrderPutAwayViewHolder, position: Int) {
        val currentPosition = holder.adapterPosition
        val poDetailsItem = poList[currentPosition]
        with(holder) {
//            if (poDetailsItem.isinspected.toBoolean()&&poDetailsItem.isdelivered.toBoolean()) {
//                poList.removeAt(position)
//            }
            binding.poNumber.text = poDetailsItem.pono.toString()
            binding.itemCode.text = poDetailsItem.itemcode
            binding.poDate.text = poDetailsItem.receiptdate.toString().substring(0, 10)
            binding.receiptNumber.text = poDetailsItem.receiptno.toString()
            binding.itemDescription.text = poDetailsItem.itemdesc.toString()
            binding.acceptedRejectedQty.text = poDetailsItem.itemqtyRejected.toString()

            itemView.setOnClickListener {
                putAwayItemClicked.putAwayItemClicked(poDetailsItem)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseOrderPutAwayViewHolder {
        val binding =
            PoItemPutAwayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseOrderPutAwayViewHolder(binding.root)
    }


    override fun getItemCount(): Int {
        return poList.size
    }

    interface PutAwayItemClick {
        fun putAwayItemClicked(poDetailsItem2: PODetailsItem2)
    }
}