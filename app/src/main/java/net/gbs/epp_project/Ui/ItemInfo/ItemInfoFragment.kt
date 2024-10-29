package net.gbs.epp_project.Ui.ItemInfo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.EditTextActionHandler
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.FragmentItemInfoBinding
import net.gbs.epp_project.Tools.ZebraScanner
class ItemInfoFragment : BaseFragmentWithViewModel<ItemInfoViewModel,FragmentItemInfoBinding>()
//    ,StatusListener,DataListener
    ,ZebraScanner.OnDataScanned {

    companion object {
        fun newInstance() = ItemInfoFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentItemInfoBinding
        get() = FragmentItemInfoBinding::inflate

//    private lateinit var barcodeReader:ZebraScanner
    private lateinit var dataWedgeBarcodeReader: ZebraScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        barcodeReader = ZebraScanner(scanner!!,this,this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataWedgeBarcodeReader = ZebraScanner(requireActivity(),this)
        setUpOrganizationsSpinner()
        setUpItemsList()
        observeGettingOrganizationsList()
        observeGettingItemInfo()
        EditTextActionHandler.OnEnterKeyPressed(binding.itemCode) {
            if (selectedOrgId!=-1){
                val itemCode = getEditTextText(binding.itemCode)
                if (itemCode.isNotEmpty()){
                    viewModel.getItemsList(selectedOrgId,itemCode)
                } else {
                    binding.itemCode.error = getString(R.string.please_scan_valid_code)
                }
            } else {
                warningDialog(requireContext(),getString(R.string.please_select_organization_first))
            }
        }
    }

    private lateinit var adapter:ItemInfoAdapter
    private fun setUpItemsList() {
        adapter = ItemInfoAdapter()
        binding.itemsList.adapter = adapter
    }

    private fun observeGettingItemInfo() {
        viewModel.getItemsListStatus.observe(requireActivity()){
            Log.d(TAG, "observeGettingItemInfo: ${it.status}")
            when(it.status){
                Status.LOADING -> {
                    loadingDialog.show()
                }
                Status.SUCCESS -> {
                    loadingDialog.hide()
                }
                else -> {
                    loadingDialog.hide()
                    Tools.warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.getItemsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                binding.itemCode.editText?.setText(it[0].iteMCODE)
                adapter.itemList = it
            } else {
                adapter.itemList = listOf()
                binding.itemCode.error =
                    getString(R.string.wrong_item_code)
            }
        }
    }

    private fun observeGettingOrganizationsList() {
        viewModel.getOrganizationsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                Status.ERROR,Status.NETWORK_FAIL -> {
                    Tools.warningDialog(requireContext(), it.message)
                    loadingDialog.hide()
                }
            }
        }
        viewModel.getOrganizationsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                organizations = it
                organizationsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,organizations)
                binding.orgCodeSpinner.setAdapter(organizationsAdapter)
            } else {
                Tools.warningDialog(requireContext(), getString(R.string.no_organizations_found))
            }
        }
    }

    private lateinit var organizationsAdapter: ArrayAdapter<Organization>
    private var organizations = listOf<Organization>()
    private var selectedOrgId = -1
    private fun setUpOrganizationsSpinner() {
        binding.orgCodeSpinner.setOnItemClickListener { _, _, i, _ ->
            selectedOrgId = organizations[i].organizationId!!
            clearScreenData()
        }
    }

    private fun clearScreenData() {
        binding.itemCode.editText?.setText("")
        adapter.itemList = listOf()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOrganizationsList()
//        barcodeReader.onResume()
        dataWedgeBarcodeReader.onResume()
        Tools.changeFragmentTitle(getString(R.string.item_info),requireActivity())
        showBackButton(requireActivity())
    }

//    override fun onStatus(p0: StatusData?) {
//        barcodeReader.onStatus(p0)
//    }
//
//    override fun onData(p0: ScanDataCollection?) {
//        requireActivity().runOnUiThread {
//            if (selectedOrgId!=-1){
//                val itemCode = barcodeReader.onData(p0)
//                Log.d(TAG, "onData: $itemCode")
//                if (itemCode.isNotEmpty()){
//                    viewModel.getItemsList(selectedOrgId,itemCode)
//                } else {
//                    binding.itemCode.error = getString(R.string.please_scan_valid_code)
//                }
//            } else {
//                warningDialog(requireContext(),getString(R.string.please_select_organization_first))
//            }
//
//            barcodeReader.restartReadData()
//        }
//    }

    override fun onPause() {
        super.onPause()

//        barcodeReader.onPause()
        dataWedgeBarcodeReader.onPause()
    }

    override fun onDataScanned(data: String) {
        if (selectedOrgId!=-1){
            val itemCode = data
            Log.d(TAG, "onData: $itemCode")
            if (itemCode.isNotEmpty()){
                viewModel.getItemsList(selectedOrgId,itemCode)
            } else {
                binding.itemCode.error = getString(R.string.please_scan_valid_code)
            }
        } else {
            warningDialog(requireContext(),getString(R.string.please_select_organization_first))
        }
    }

}