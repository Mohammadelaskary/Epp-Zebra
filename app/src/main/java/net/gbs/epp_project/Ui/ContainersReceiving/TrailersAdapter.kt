package net.gbs.epp_project.Ui.ContainersReceiving

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.gbs.epp_project.Ui.ContainersReceiving.CustomerDataSearch.Trailer
import net.gbs.epp_project.databinding.TrailersItemBinding

class TrailersAdapter(var trailers:List<Trailer>): RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder>() {
    inner class TrailersViewHolder(itemView: View):ViewHolder(itemView){
        val binding = TrailersItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailersViewHolder {
        val binding = TrailersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TrailersViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onBindViewHolder(holder: TrailersViewHolder, position: Int) {
        with(holder){
            binding.trailerNo.text = trailers[position].trailerNumber
            binding.containers.text = trailers[position].containers
//            if (position==trailers.size)
//                binding.line.visibility = GONE
//            else
//                binding.line.visibility = VISIBLE
        }
    }
}