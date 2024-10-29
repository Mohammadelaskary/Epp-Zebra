package net.gbs.epp_project.Ui.ContainersReceiving.CustomerDataSearch

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.Navigation
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.databinding.FragmentCustomerNameSearchBinding

class CustomerNameSearchFragment : BaseFragmentWithViewModel<CustomerNameSearchViewModel,FragmentCustomerNameSearchBinding>(),TruckAdapter.OnTruckItemSelected, OnClickListener {

    companion object {
        fun newInstance() = CustomerNameSearchFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCustomerNameSearchBinding
        get() = FragmentCustomerNameSearchBinding::inflate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTrucksRecyclerView()
        observeGettingTrucks()
        Tools.attachButtonsToListener(this,binding.addNewTruck,binding.save)
    }

    private fun observeGettingTrucks() {
        viewModel.getTrucksLiveData.observe(requireActivity()){
            trucksAdapter.trucks = it
        }
    }

    private lateinit var trucksAdapter:TruckAdapter
    private fun setUpTrucksRecyclerView() {
        trucksAdapter = TruckAdapter(requireContext(),this)
        binding.trucksList.adapter = trucksAdapter
    }

    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.containers_receiving),requireActivity())
        viewModel.getTrucks()
        showBackButton(requireActivity())
    }

    override fun OnTruckClicked(truckNo: String) {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.add_new_truck->{
                Navigation.findNavController(v).navigate(R.id.action_customerNameSearchFragment_to_addNewTruckFragment)
            }
        }
    }
}