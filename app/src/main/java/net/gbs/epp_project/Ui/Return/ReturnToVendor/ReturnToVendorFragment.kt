package net.gbs.epp_project.Ui.Return.ReturnToVendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.RETURN_TO_VENDOR
import net.gbs.epp_project.Base.BundleKeys.RETURN_TO_WAREHOUSE
import net.gbs.epp_project.Base.BundleKeys.SOURCE_KEY
import net.gbs.epp_project.Model.ApiRequestBody.ReturnMaterialBody
import net.gbs.epp_project.Model.POItem
import net.gbs.epp_project.Model.POLineReturn
import net.gbs.epp_project.Model.PurchaseOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.EditTextActionHandler
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.attachButtonsToListener
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.containsOnlyDigits
import net.gbs.epp_project.Tools.Tools.datePicker
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.showSuccessAlerter
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.FragmentReturnToVendorBinding
import net.gbs.epp_project.Tools.ZebraScanner
class ReturnToVendorFragment : BaseFragmentWithViewModel<ReturnToVendorViewModel,FragmentReturnToVendorBinding>(),
//    Scanner.DataListener, Scanner.StatusListener,
    ZebraScanner.OnDataScanned,
    View.OnClickListener {

    companion object {
        fun newInstance() = ReturnToVendorFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReturnToVendorBinding
        get() = FragmentReturnToVendorBinding::inflate

    private lateinit var barcodeReader : ZebraScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
//    private var organizationId:Int? = null
//    private var source = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcodeReader = ZebraScanner(requireActivity(),this)
//        organizationId = arguments?.getInt(ORGANIZATION_ID_KEY)


        binding.showCalendar.setOnClickListener {
            datePicker(requireContext(),binding.returnDate).show(requireActivity().supportFragmentManager,getString(R.string.select_a_date))
        }
//        viewModel.getTodayDate()
        clearInputLayoutError(binding.poNumber,binding.returnDate,binding.itemCode,binding.quantity)
        attachButtonsToListener(this,binding.doReturn)
        onSearchButtonClicked()
        observeSearchingPoNumber()
        observeGettingPoItems()
        observeReturn()
//        observeGettingDate()
        EditTextActionHandler.OnEnterKeyPressed(binding.itemCode){
            val itemCode = getEditTextText(binding.itemCode)
            scannedItem = poItemsList.find { it.itemcode == itemCode }
            if (scannedItem!=null){
                binding.itemDataGroup.visibility = VISIBLE
                fillItemData()
            } else {
                binding.itemDataGroup.visibility = GONE
                binding.itemCode.error =
                    getString(R.string.item_code_is_wrong_or_doesn_t_belong_to_this_purchase_order)
            }
        }
    }

//    private fun observeGettingDate() {
//        viewModel.getDateStatus.observe(requireActivity()){
//            when(it.status){
//                Status.LOADING  -> {
//                    loadingDialog.show()
//                    binding.showCalendar.isEnabled = false
//                }
//                Status.SUCCESS ->{
//                    loadingDialog.hide()
//                    binding.showCalendar.isEnabled = false
//                }
//                else -> {
//                    loadingDialog.hide()
//                    binding.showCalendar.isEnabled = true
//                }
//            }
//        }
//        viewModel.getDateLiveData.observe(requireActivity()){
//            binding.returnDate.editText?.setText(it.substring(0,10))
//        }
//    }

    private fun observeReturn() {
        viewModel.returnMaterialStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> {
                    loadingDialog.hide()
                    clearItemData()
                    showSuccessAlerter(it.message,requireActivity())
                }
                else -> {
                    loadingDialog.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
    }

    private fun clearItemData() {
        binding.itemDataGroup.visibility = GONE
        binding.itemCode.editText?.setText("")
        binding.itemDesc.text = ""
        binding.quantity.editText?.setText("")
        binding.uom.text = ""
    }

    private var poItemsList = listOf<POItem>()
    private fun observeGettingPoItems() {
        viewModel.poItemsStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING ->{
                    loadingDialog.show()
                    binding.purchaseOrderNumberDataLayout.visibility = GONE
                }
                Status.SUCCESS -> {
                    loadingDialog.hide()
                    binding.purchaseOrderNumberDataLayout.visibility = VISIBLE
                }
                else -> {
                    loadingDialog.hide()
                    binding.poNumber.error = it.message
                    binding.purchaseOrderNumberDataLayout.visibility = GONE
                }
            }
        }

        viewModel.poItemsLiveData.observe(requireActivity()){
            if (it.isNotEmpty()) {
                poItemsList = it
                binding.supplier.text = purchaseOrder.supplier
            } else
                binding.poNumber.error = getString(R.string.this_purchase_order_has_no_items)
        }
    }

    private lateinit var purchaseOrder: PurchaseOrder
    private fun observeSearchingPoNumber() {
        viewModel.purchaseOrderStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                else -> {
                    loadingDialog.hide()
                    binding.poNumber.error = it.message
                }
            }
        }
        viewModel.purchaseOrderLiveData.observe(requireActivity()){
            if (it.isNotEmpty()) {
//                if (it[0].orgId==organizationId) {
                purchaseOrder = it[0]
                viewModel.getPurchaseOrderItemListReturn(purchaseOrder.purchaseOrderNumber)
//                } else {
//                    binding.poNumber.error =
//                        getString(R.string.wrong_purchase_order_for_selected_organization)
//                }
            } else
                binding.poNumber.error = getString(R.string.wrong_po_number)
        }
    }

    private fun onSearchButtonClicked() {
        binding.poNumber.setEndIconOnClickListener {
            val poNumber = binding.poNumber.editText?.text.toString().trim()
            if (poNumber.isNotEmpty()){
                viewModel.getPurchaseOrdersList(poNumber)
                clearItemData()
            } else {
                binding.poNumber.error = getString(R.string.please_enter_po_number)
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        source = arguments?.getString(SOURCE_KEY)!!
//        when(source){
//            RETURN_TO_VENDOR -> {
                Tools.changeFragmentTitle(getString(R.string.return_to_vendor), requireActivity())
                binding.poNumber.hint = getString(R.string.purchase_order_number)
//            }
//            RETURN_TO_WAREHOUSE -> {
//                Tools.changeFragmentTitle(getString(R.string.return_to_warehouse), requireActivity())
//                binding.poNumber.hint = getString(R.string.purchase_order_number)
//            }
//        }
        showBackButton(requireActivity())
        binding.returnDate.editText?.setText(viewModel.getDisplayTodayDate())
        barcodeReader.onResume()
    }

    override fun onPause() {
        super.onPause()
        barcodeReader.onPause()
    }
    private var scannedItem :POItem? = null
//    override fun onData(p0: ScanDataCollection?) {
//        requireActivity().runOnUiThread {
//            val scannedText = barcodeReader.onData(p0)
//            scannedItem = poItemsList.find { it.itemcode == scannedText }
//            if (scannedItem!=null){
//                binding.itemDataGroup.visibility = VISIBLE
//                fillItemData()
//            } else {
//                binding.itemDataGroup.visibility = GONE
//                binding.itemCode.error =
//                    getString(R.string.item_code_is_wrong_or_doesn_t_belong_to_this_purchase_order)
//            }
//            barcodeReader.restartReadData()
//        }
//    }

    private fun fillItemData() {
        binding.itemCode.editText?.setText(scannedItem?.itemcode)
        binding.itemDesc.text = scannedItem?.itemdesc
        binding.uom.text = scannedItem?.itemuom
        binding.quantity.editText?.setText(scannedItem?.transQty.toString())
    }

//    override fun onStatus(p0: StatusData?) {
//        barcodeReader.onStatus(p0)
//    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.do_return ->{
                val returnDate = binding.returnDate.editText?.text.toString().trim()
                val quantityText    = binding.quantity.editText?.text.toString().trim()
                if (isReadyToSave(returnDate,quantityText)){
                    viewModel.returnMaterial(
                        ReturnMaterialBody(
                            poHeaderId = purchaseOrder.poHeaderId?.toInt(),
                            transactionDate = viewModel.getTodayDate(),
                            poLines = listOf(
                                POLineReturn(
                                    transactioNID = scannedItem?.transactioNID,
                                    transactioNTYPE = scannedItem?.transactioNTYPE,
                                    shipToOrganizationId = scannedItem?.shipToOrganizationId,
                                    poLineId = scannedItem?.poLineId,
                                    receiptNum = scannedItem?.receiptno,
                                    quantityReturned = quantityText.toInt()
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    private fun isReadyToSave(returnDate:String,qty:String):Boolean{
        var isReady = true
        if (returnDate.isEmpty()){
            binding.returnDate.error = getString(R.string.select_a_date)
            isReady = false
        }
        if (scannedItem == null){
            binding.itemCode.error = getString(R.string.please_scan_or_enter_item_code)
            isReady = false
        }
        if (qty.isEmpty()){
            binding.quantity.error = getString(R.string.please_enter_qty)
            isReady = false
        }
        if (!containsOnlyDigits(qty)){
            binding.quantity.error = getString(R.string.please_enter_valid_qty)
            isReady = false
        } else {
            if (qty.toInt()>scannedItem?.transQty!!){
                binding.quantity.error = getString(R.string.quantity_must_be_less_than_or_equal_to)+scannedItem?.transQty!!
                isReady = false
            }
        }
        return isReady
    }

    override fun onDataScanned(data: String) {
        val scannedText = data
        scannedItem = poItemsList.find { it.itemcode == scannedText }
        if (scannedItem!=null){
            binding.itemDataGroup.visibility = VISIBLE
            fillItemData()
        } else {
            binding.itemDataGroup.visibility = GONE
            binding.itemCode.error =
                getString(R.string.item_code_is_wrong_or_doesn_t_belong_to_this_purchase_order)
        }
    }

}