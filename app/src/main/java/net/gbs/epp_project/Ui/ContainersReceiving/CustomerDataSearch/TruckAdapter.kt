package net.gbs.epp_project.Ui.ContainersReceiving.CustomerDataSearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.gbs.epp_project.R
import net.gbs.epp_project.Ui.ContainersReceiving.TrailersAdapter
import net.gbs.epp_project.databinding.TruckItemLayoutBinding

class TruckAdapter(private val context: Context,val onTruckItemSelected: OnTruckItemSelected):
    RecyclerView.Adapter<TruckAdapter.TruckViewHolder>() {
    var trucks:List<Truck> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class TruckViewHolder(itemView: View) : ViewHolder(itemView) {
        val binding = TruckItemLayoutBinding.bind(itemView)
    }
    interface OnTruckItemSelected {
        fun OnTruckClicked(truckNo:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val binding = TruckItemLayoutBinding.inflate((LayoutInflater.from(parent.context)),parent,false)
        return TruckViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return trucks.size
    }

    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        val truck = trucks[position]
        with(holder){
            binding.truckNo.text = truck.truckNo
            setUpRecyclerView(binding.trailersNumbers,truck.trailers)
            binding.root.setOnClickListener{
                onTruckItemSelected.OnTruckClicked(truck.truckNo)
                binding.root.setCardBackgroundColor(context.getColor(R.color.green))
            }
        }
    }

    private fun setUpRecyclerView(trailersNumbers: RecyclerView, trailers: List<Trailer>) {
        val trailersAdapter = TrailersAdapter(trailers)
        trailersNumbers.adapter = trailersAdapter
    }
}