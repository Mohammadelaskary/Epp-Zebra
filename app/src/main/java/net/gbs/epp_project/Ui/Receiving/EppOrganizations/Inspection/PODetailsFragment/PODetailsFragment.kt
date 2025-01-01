package com.example.mobco.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.PURCHASE_ORDER_KEY
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.PODetailsItem
import net.gbs.epp_project.Model.PurchaseOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Ui.Receiving.EppOrganizations.Inspection.PODetailsFragment.PODetailsViewModel
import net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.StartReceiving.PoDetailsAdapter
import net.gbs.epp_project.databinding.FragmentPODetailsBinding

class PODetailsFragment :
    BaseFragmentWithViewModel<PODetailsViewModel,FragmentPODetailsBinding>(), PoDetailsAdapter.OnPOLineClicked {

    companion object {
        fun newInstance() = PODetailsFragment()
    }


    private lateinit var purchaseOrder : PurchaseOrder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.p_o_details),requireActivity())
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPODetailsBinding
        get() = FragmentPODetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        purchaseOrder = Gson().fromJson(requireArguments().getString(PURCHASE_ORDER_KEY),PurchaseOrder::class.java)
        fillHeaderData()

        setUpRecyclerView()
        viewModel.getPoOrganizations(purchaseOrder.poHeaderId!!)

        setUpOrganizationsSpinner()
        observeOrganizations()

    }
    private fun observeOrganizations() {
        viewModel.getPoOrganizationsStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                Status.ERROR -> {
                    loadingDialog!!.hide()
                    binding.org.error = it.message
                }
                else -> {
                    loadingDialog!!.hide()
                    Tools.warningDialog(requireContext(), it.message)
                }
            }
        }
        viewModel.getPoOrganizationsLiveData.observe(requireActivity()){
            poOrganizations = it
            organizationsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,poOrganizations)
            binding.organizationSpinner.setAdapter(organizationsAdapter)
        }
    }
    private lateinit var organizationsAdapter: ArrayAdapter<Organization>
    private var poOrganizations = listOf<Organization>()
    private var selectedOrganization: Organization? = null
    private fun setUpOrganizationsSpinner() {
        organizationsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,poOrganizations)
        binding.organizationSpinner.setAdapter(organizationsAdapter)
        binding.organizationSpinner.setOnItemClickListener(){ adapterView: AdapterView<*>, view2: View, position: Int, l: Long ->
            selectedOrganization = poOrganizations[position]
            viewModel.getPurchaseOrderDetailsList(
                loadingDialog!!,
                poDetailsAdapter,
                selectedOrganization!!.organizationId!!,
                purchaseOrder.poHeaderId.toString()
            )
        }
    }

    private fun fillHeaderData() {
        binding.poHeader.vendor.text = purchaseOrder.supplier
        binding.poHeader.poNumber.text = purchaseOrder.pono.toString()
        binding.poHeader.poCreatorName.text = purchaseOrder.poCreatedUser
        binding.poHeader.poType.text = purchaseOrder.potype
    }

    private lateinit var poDetailsAdapter: PoDetailsAdapter
    private fun setUpRecyclerView() {
        poDetailsAdapter = PoDetailsAdapter(requireContext(),this)
        binding.itemsList.adapter = poDetailsAdapter
    }

    override fun onPOLineClicked(po: PODetailsItem) {
    }
}