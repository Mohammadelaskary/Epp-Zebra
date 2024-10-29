package net.gbs.epp_project.Ui.ContainersReceiving.AddNewTruck

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.back
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.showErrorAlerter
import net.gbs.epp_project.Tools.Tools.showSuccessAlerter
import net.gbs.epp_project.Ui.ContainersReceiving.CustomerDataSearch.Trailer
import net.gbs.epp_project.Ui.ContainersReceiving.TrailersAdapter
import net.gbs.epp_project.databinding.FragmentAddNewTruckBinding

class AddNewTruckFragment : BaseFragmentWithViewModel<AddNewTruckViewModel,FragmentAddNewTruckBinding>(),AddNewTrailerDialog.OnAddTrailerButtonClicked {

    companion object {
        fun newInstance() = AddNewTruckFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddNewTruckBinding
        get() = FragmentAddNewTruckBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTrailersRecyclerView()
        setUpAddTrailersDialog()
        clearInputLayoutError(binding.truckNo)
        binding.addTrailer.setOnClickListener {
            addNewTrailerDialog.show()
        }
        binding.save.setOnClickListener {
            val truckNo = getEditTextText(binding.truckNo)
            if (truckNo.isNotEmpty()){
                if (trailers.isNotEmpty()){
                    showSuccessAlerter(getString(R.string.truck_add_successfully),requireActivity())
                    back(this)
                } else {
                    showErrorAlerter(getString(R.string.please_enter_trailers),requireActivity())
                }
            } else {
                binding.truckNo.error = getString(R.string.please_enter_truck_number)
            }
        }
    }
    private lateinit var addNewTrailerDialog :AddNewTrailerDialog
    private fun setUpAddTrailersDialog() {
        addNewTrailerDialog = AddNewTrailerDialog(requireContext(),this)
    }

    private val trailers:MutableList<Trailer> = mutableListOf()
    private lateinit var trailersAdapter: TrailersAdapter
    private fun setUpTrailersRecyclerView() {
        trailersAdapter = TrailersAdapter(trailers)
        binding.trailers.adapter = trailersAdapter
    }

    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.add_new_truck),requireActivity())
        showBackButton(requireActivity())
    }

    override fun onTrailerDataAdded(trailerNo: String, containers: String) {
        addNewTrailerDialog.dismiss()
        trailers.add(Trailer(trailerNo,containers))
        trailersAdapter.notifyItemInserted(trailers.size)
    }
}