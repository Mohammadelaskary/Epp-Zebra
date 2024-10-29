package net.gbs.epp_project.Ui.Receiving.EppOrganizations.ItemInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.Model.PurchaseOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.EditTextActionHandler
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.databinding.FragmentPosInfoBinding
import net.gbs.epp_project.Tools.ZebraScanner
class POsInfoFragment : BaseFragmentWithViewModel<POsInfoViewModel,FragmentPosInfoBinding>(),
//    DataListener,StatusListener
        ZebraScanner.OnDataScanned
{

    companion object {
        fun newInstance() = POsInfoFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPosInfoBinding
        get() = FragmentPosInfoBinding::inflate

    private lateinit var barcodeReader :ZebraScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcodeReader = ZebraScanner(requireActivity(),this)
        observeItemInfo()
        setUpRecyclerView()
        var po :PurchaseOrder?=null
        if (arguments!=null) {
            po = requireArguments().getString(BundleKeys.PURCHASE_ORDER_KEY)
                ?.let { PurchaseOrder.fromJson(it) }
        }
        if (po!=null){
            binding.itemCode.visibility = GONE
            viewModel.getPoReceivedList(po.pono!!)
        }
        EditTextActionHandler.OnEnterKeyPressed (binding.itemCode){
            val itemCode = Tools.getEditTextText(binding.itemCode)
            if (itemCode.isNotEmpty()){
                viewModel.getItemInfo(itemCode)
            } else {
                binding.itemCode.error = getString(R.string.please_scan_or_enter_item_code)
            }
        }
    }
    private var poList : List<PODetailsItem2> = listOf()
    private lateinit var adapter :POsInfoAdapter
    private fun setUpRecyclerView() {
        adapter = POsInfoAdapter()
        binding.purchaseOrders.adapter = adapter
    }

    private fun observeItemInfo() {
        viewModel.getItemInfoStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                    loadingDialog.show()
                }
                Status.SUCCESS -> {
                    loadingDialog.hide()
                }
                else -> {
                    adapter.poList= listOf()
                    loadingDialog.hide()
                    Tools.warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.getItemInfoLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                adapter.poList = it
            } else {
                adapter.poList = listOf()
                binding.itemCode.error =
                    getString(R.string.this_item_code_doesn_t_found_in_any_purchase_orders)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.item_pos),requireActivity())
        Tools.showBackButton(requireActivity())
        barcodeReader.onResume()
    }

    override fun onPause() {
        super.onPause()
        barcodeReader.onPause()
    }

//    override fun onData(p0: ScanDataCollection?) {
//        requireActivity().runOnUiThread(){
//            val itemCode = barcodeReader.onData(p0)
//            binding.itemCode.editText?.setText(itemCode)
//            if (itemCode.isNotEmpty()){
//                viewModel.getItemInfo(itemCode)
//            } else {
//                binding.itemCode.error = getString(R.string.please_scan_or_enter_item_code)
//            }
//            barcodeReader.restartReadData()
//        }
//    }
//
//    override fun onStatus(p0: StatusData?) {
//        barcodeReader.onStatus(p0)
//    }

    override fun onDataScanned(data: String) {
        val itemCode = data
        binding.itemCode.editText?.setText(itemCode)
        if (itemCode.isNotEmpty()){
            viewModel.getItemInfo(itemCode)
        } else {
            binding.itemCode.error = getString(R.string.please_scan_or_enter_item_code)
        }
    }


}