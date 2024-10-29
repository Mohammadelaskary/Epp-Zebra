package net.gbs.epp_project.Ui.FinishedProductsItemInfo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.ORGANIZATION_ID_KEY
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.EditTextActionHandler
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.FragmentFinishedProductsItemInfoBinding
import net.gbs.epp_project.Tools.ZebraScanner
class FinishedProductsItemInfoFragment : BaseFragmentWithViewModel<FinishedProductsItemInfoViewModel,FragmentFinishedProductsItemInfoBinding>(),
//    StatusListener,DataListener
        ZebraScanner.OnDataScanned
{

    companion object {
        fun newInstance() = FinishedProductsItemInfoFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFinishedProductsItemInfoBinding
        get() = FragmentFinishedProductsItemInfoBinding::inflate

    private lateinit var barcodeReader:ZebraScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var orgId: Int = -1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcodeReader = ZebraScanner(requireActivity(),this)
        orgId = requireArguments().getInt(ORGANIZATION_ID_KEY)
        setUpItemsList()
        observeGettingItemInfo()
        EditTextActionHandler.OnEnterKeyPressed(binding.itemCode) {
            if (orgId!=-1){
                val itemCode = getEditTextText(binding.itemCode)
                if (itemCode.isNotEmpty()){
                    viewModel.getItemsList(orgId,itemCode)
                } else {
                    binding.itemCode.error = getString(R.string.please_scan_valid_code)
                }
            } else {
                warningDialog(requireContext(),getString(R.string.please_select_organization_first))
            }
        }
    }

    private lateinit var adapter:FinishedProductsItemInfoAdapter
    private fun setUpItemsList() {
        adapter = FinishedProductsItemInfoAdapter()
        binding.itemsList.adapter = adapter
    }

    private fun observeGettingItemInfo() {
        viewModel.getItemsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                    loadingDialog.show()
                }
                Status.SUCCESS -> {
                    loadingDialog.hide()
                }
                Status.ERROR -> {
                    loadingDialog.hide()
                    binding.itemCode.error = it.message
                }
                else -> {
                    loadingDialog.hide()
                    Tools.warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.getItemsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                binding.itemCode.editText?.setText(it[0].itemCode)
                adapter.itemList = it
            } else {
                adapter.itemList = listOf()
                binding.itemCode.error =
                    getString(R.string.this_item_code_doesn_t_found_in_any_purchase_orders)
            }
        }
    }





    override fun onResume() {
        super.onResume()
        barcodeReader.onResume()
        Tools.changeFragmentTitle(getString(R.string.item_info),requireActivity())
        showBackButton(requireActivity())
    }

//    override fun onStatus(p0: StatusData?) {
//        barcodeReader.onStatus(p0)
//    }
//
//    override fun onData(p0: ScanDataCollection?) {
//        requireActivity().runOnUiThread {
//            if (orgId!=-1){
//                val itemCode = barcodeReader.onData(p0)
//                Log.d(TAG, "onData: $itemCode")
//                if (itemCode.isNotEmpty()){
//                    viewModel.getItemsList(orgId,itemCode)
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
        barcodeReader.onPause()
    }

    override fun onDataScanned(data: String) {
        if (orgId!=-1){
            val itemCode = data
            Log.d(TAG, "onData: $itemCode")
            if (itemCode.isNotEmpty()){
                viewModel.getItemsList(orgId,itemCode)
            } else {
                binding.itemCode.error = getString(R.string.please_scan_valid_code)
            }
        } else {
            warningDialog(requireContext(),getString(R.string.please_select_organization_first))
        }
    }

}