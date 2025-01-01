package net.gbs.epp_project.Ui.Return.ReturnToWip

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.ORGANIZATION_ID_KEY
import net.gbs.epp_project.Model.ApiRequestBody.TransferMaterialBody
import net.gbs.epp_project.Model.Locator
import net.gbs.epp_project.Model.Lot
import net.gbs.epp_project.Model.OnHandItemLot
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.SubInventory
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.EditTextActionHandler
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.showSuccessAlerter
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.TransactMoveOrderFragment
import net.gbs.epp_project.databinding.FragmentReturnToWipBinding
import net.gbs.epp_project.Tools.ZebraScanner
class ReturnToWipFragment : BaseFragmentWithViewModel<ReturnToWipViewModel,FragmentReturnToWipBinding>(),
//    DataListener,StatusListener
        ZebraScanner.OnDataScanned
{

    companion object {
        fun newInstance() = ReturnToWipFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReturnToWipBinding
        get() = FragmentReturnToWipBinding::inflate
    private lateinit var barcodeReader:ZebraScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var orgId: Int = -1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcodeReader = ZebraScanner(requireActivity(),this)
        orgId = arguments?.getInt(ORGANIZATION_ID_KEY)!!
        observeGettingData()
        setUpSpinners()
        setUpDateDialog()
        clearInputLayoutError(
            binding.subInventoryFrom,
            binding.locatorFrom,
            binding.itemCode,
            binding.transferQty,
            binding.subInventoryTo,
            binding.lot,
            )
        binding.transfer.setOnClickListener {
            val qty = getEditTextText(binding.transferQty)
            val transferDate = getEditTextText(binding.date)
            if (isReadyToSave(qty,transferDate)){
                val transferBody = TransferMaterialBody(
                    organizationId = orgId,
                    inventoryItemId = itemData[0].inventoryItemId,
                    subinventoryCodeFrom = selectedSubInventoryCodeFrom,
                    locatorCodeFrom = selectedLocatorCodeFrom,
                    subinventoryCodeTo = selectedSubInventoryCodeTo,
                    qty = qty.toDouble(),
                    transactionDate = viewModel.getTodayDate(),
                    lotNum = selectedlot
                )
                viewModel.transferMaterial(transferBody)
            }
        }



        EditTextActionHandler.OnEnterKeyPressed(binding.itemCode){
            val itemCode = getEditTextText(binding.itemCode)
            itemCodeScanned(itemCode)
        }
    }

    private fun setUpDateDialog() {
        binding.date.setOnClickListener {
            Tools.datePicker(requireContext(), binding.date)
                .show(requireActivity().supportFragmentManager,getString(R.string.select_a_date))

        }
    }

    private fun setUpSpinners() {
        setUpSubInventorySpinner()
        setUpLocatorsSpinner()
    }

    private fun observeGettingData() {
//        observeGettingDate()
        observeGettingSubInventoryList()
        observeGettingItemData()
        observeTransferingItem()
    }

    private fun observeTransferingItem() {
        viewModel.transferMaterialStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> {
                    loadingDialog!!.hide()
                    showSuccessAlerter(it.message,requireActivity())
                    clearItemData()
                }
                else -> {
                    loadingDialog!!.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
    }

    private fun clearItemData() {
        itemData = listOf()
        binding.itemCode.editText?.setText("")
        binding.itemDesc.visibility = GONE
        binding.onHandQty.visibility = GONE
        binding.subInventoryFromSpinner.setText("",false)
        clearSubInventoryFrom()
        clearLocatorFrom()
        clearLot()
        binding.transferQty.editText?.setText("")
        binding.subInventoryToSpinner.setText("",false)
        binding.lotSpinner.setText("",false)
    }

    private fun clearLot() {
        lotList = mutableListOf()
        lotAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,lotList)
        binding.lotSpinner.setAdapter(lotAdapter)
        binding.lotSpinner.setText("",false)
    }

    private fun clearLocatorFrom() {
        locatorsFromList = mutableListOf()
        locatorsFromAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,locatorsFromList)
        binding.locatorFromSpinner.setAdapter(locatorsFromAdapter)
        binding.locatorFromSpinner.setText("",false)
    }

    private fun clearSubInventoryFrom() {
        subInventoryFromList = mutableListOf()
        subInventoryFromAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,subInventoryFromList)
        binding.subInventoryFromSpinner.setAdapter(subInventoryFromAdapter)
        binding.subInventoryFromSpinner.setText("",false)
    }

    private var itemData:List<OnHandItemLot> = listOf()
    private fun observeGettingItemData() {
        viewModel.getItemsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    warningDialog(requireContext(),it.message)
                    clearItemData()
                }
            }
        }
        viewModel.getItemsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                Log.d(TAG, "onBarcodeReadDataApi: ${it[0].itemCode}")
                itemData = it
                fillItemData()
                fillLotList()
                fillSubInventoryList()
            } else {
                clearItemData()
                binding.itemCode.error =
                    getString(R.string.scanned_item_code_is_wrong_or_doesn_t_belong_to_selected_sub_inventory_or_selected_locator)
                itemData = listOf()
            }
        }
    }
    private var lotList = mutableListOf<Lot>()
    private lateinit var lotAdapter: ArrayAdapter<Lot>
    private var selectedlot: String? = null
    private fun fillLotList() {
        itemData.forEach(fun(item: OnHandItemLot) {
            val lot = lotList.find { it.lotName == item.lotNum }
            if (lot==null)
                lotList.add(
                    Lot(
                        lotName = item.lotNum
                    )
                )

        })
        lotAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                lotList
            )
        binding.lotSpinner.setAdapter(lotAdapter)
        if (lotList.size==1) {
            if (itemData.isNotEmpty()) {
                binding.lotSpinner.setText(lotList[0].lotName)
                selectedlot = lotList[0].lotName
            } else {
                warningDialog(requireContext(),getString(R.string.please_scan_or_enter_item_code))
            }
        }
        binding.lotSpinner.setOnItemClickListener { _, _, index, _ ->
            selectedlot = lotList[index].lotName
        }
    }

    private fun fillSubInventoryList() {
        itemData.forEach(fun(item: OnHandItemLot) {
            val subInventory = subInventoryFromList.find { it.subInventoryCode == item.subinventory }
            if (subInventory==null)
                subInventoryFromList.add(
                    SubInventory(
                        subInventoryCode = item.subinventory
                    )
                )

        })
        subInventoryFromAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                subInventoryFromList
            )
        binding.subInventoryFromSpinner.setAdapter(subInventoryFromAdapter)
        if (subInventoryFromList.size==1) {
            if (itemData.isNotEmpty()) {
                binding.subInventoryFromSpinner.setText(subInventoryFromList[0].subInventoryCode)
                selectedSubInventoryCodeFrom = subInventoryFromList[0].subInventoryCode
                fillFromLocatorList()
                subInvType = TransactMoveOrderFragment.SubInvType.FROM
            } else {
                warningDialog(requireContext(),getString(R.string.please_scan_or_enter_item_code))
            }
        }
    }

    private fun fillItemData() {
        binding.itemCode.editText?.setText(itemData[0].itemCode)
        binding.itemDesc.visibility = VISIBLE
        binding.itemDesc.text = itemData[0].itemDescription
        binding.onHandQty.visibility = VISIBLE
        binding.onHandQty.text = itemData[0].onhand.toString()
        if (itemData.size==1){
            binding.subInventoryFromSpinner.setText(itemData[0].subinventory)
            selectedSubInventoryCodeFrom = itemData[0].subinventory
            binding.locatorFromSpinner.setText(itemData[0].locator)
            selectedLocatorCodeFrom = itemData[0].locator
        }
    }

//    private fun observeGettingDate() {
//        viewModel.getDateStatus.observe(requireActivity()){
//            when(it.status){
//                Status.LOADING -> {
//                    loadingDialog!!.show()
//                    binding.date.isEnabled = false
//                }
//                Status.SUCCESS -> {
//                    loadingDialog!!.hide()
//                    binding.date.isEnabled = false
//                    viewModel.getSubInvertoryList(orgId)
//                }
//                else -> {
//                    loadingDialog!!.hide()
//                    binding.date.error = it.message
//                    binding.date.isEnabled = true
//                    viewModel.getSubInvertoryList(orgId)
//                }
//            }
//        }
//        viewModel.getDateLiveData.observe(requireActivity()){
//            binding.date.editText?.setText(it.substring(0,10))
//        }
//    }
    private var subInventoryFromList = mutableListOf<SubInventory>()
    private var subInventoryList = listOf<SubInventory>()
    private lateinit var subInventoryToAdapter: ArrayAdapter<SubInventory>
    private lateinit var subInventoryFromAdapter: ArrayAdapter<SubInventory>
    private var selectedSubInventoryCodeFrom: String? = null
    private var selectedSubInventoryCodeTo: String? = null
    private var subInvType: TransactMoveOrderFragment.SubInvType = TransactMoveOrderFragment.SubInvType.FROM
    private fun observeGettingSubInventoryList() {
        viewModel.getSubInvertoryListStatus.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    Tools.warningDialog(requireContext(), it.message)
                }
            }
        }
        viewModel.getSubInvertoryListLiveData.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                subInventoryList = it
                subInventoryToAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    subInventoryList
                )
                binding.subInventoryToSpinner.setAdapter(subInventoryToAdapter)
            } else {
                Tools.warningDialog(
                    requireContext(),
                    getString(R.string.no_sub_inventories_for_this_org_id)
                )
            }
        }
    }

    private fun setUpSubInventorySpinner() {
        binding.subInventoryFromSpinner.setOnItemClickListener { _, _, selectedIndex, _ ->
            selectedSubInventoryCodeFrom = subInventoryFromList[selectedIndex].subInventoryCode
            fillFromLocatorList()
            subInvType = TransactMoveOrderFragment.SubInvType.FROM
        }
        binding.subInventoryToSpinner.setOnItemClickListener { _, _, selectedIndex, _ ->
            selectedSubInventoryCodeTo = subInventoryList[selectedIndex].subInventoryCode
            subInvType = TransactMoveOrderFragment.SubInvType.To
        }
    }

    private fun fillFromLocatorList() {
        Log.d(TAG, "fillFromLocatorListSelectedSubInventoryCodeFrom: $selectedSubInventoryCodeFrom")
        itemData.forEach(fun(item: OnHandItemLot) {
            if (item.subinventory == selectedSubInventoryCodeFrom && item.itemCode == getEditTextText(
                    binding.itemCode
                )
            ) {
                val locator = locatorsFromList.find { it.locatorCode == item.locator}
                if (locator==null)
                    locatorsFromList.add(
                        Locator(
                            locatorCode = item.locator
                        )
                    )
            }
        })
        locatorsFromAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,locatorsFromList)
        binding.locatorFromSpinner.setAdapter(locatorsFromAdapter)

        if (locatorsFromList.size==1){
            binding.locatorFromSpinner.setText(locatorsFromList[0].locatorCode)
            selectedLocatorCodeFrom = locatorsFromList[0].locatorCode
        }
    }

    private var locatorsFromList = mutableListOf<Locator>()
    private lateinit var locatorsFromAdapter: ArrayAdapter<Locator>
    private var selectedLocatorCodeFrom: String? = null




    private fun setUpLocatorsSpinner() {
        locatorsFromAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            locatorsFromList
        )
        binding.locatorFromSpinner.setAdapter(locatorsFromAdapter)
        binding.locatorFromSpinner.setOnItemClickListener { _, _, selectedIndex, _ ->
            selectedLocatorCodeFrom = locatorsFromList[selectedIndex].locatorCode
        }

    }
    


    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.return_from_wip_to_production),requireActivity())
        showBackButton(requireActivity())
//        viewModel.getTodayDate()
        viewModel.getSubInvertoryList(orgId)
        binding.date.editText?.setText(viewModel.getDisplayTodayDate())
        barcodeReader.onResume()
    }



    override fun onPause() {
        super.onPause()
        barcodeReader.onPause()
    }

//    override fun onData(p0: ScanDataCollection?) {
//        requireActivity().runOnUiThread {
//            val scannedText = barcodeReader.onData(p0)
//            onBarcodeReadData(scannedText)
//            barcodeReader.restartReadData()
//        }
//    }

    private fun onBarcodeReadData(scannedText:String) {
        Log.d(TAG, "onBarcodeReadData: $scannedText")
        Log.d(TAG, "onBarcodeReadData: ${getEditTextText(binding.itemCode)}")
        if (getEditTextText(binding.itemCode).isEmpty()){
            itemCodeScanned(scannedText)
        } else  {
            if (getEditTextText(binding.locatorFrom).isEmpty())
                locatorFromCodeScanned(scannedText)
        }
    }

    private fun itemCodeScanned(itemCode: String) {
            viewModel.getItemInfo(itemCode,orgId)
    }

    private fun locatorFromCodeScanned(locatorFrom: String) {
        if (selectedSubInventoryCodeFrom!=null){
            val locator = locatorsFromList.find { it.locatorCode == locatorFrom }
            if (locator!=null){
                selectedLocatorCodeFrom = locator.locatorCode
                binding.locatorFrom.editText?.setText(locator.locatorCode)
            } else {
                binding.locatorFromSpinner.setText("",false)
                warningDialog(requireContext(),getString(R.string.wrong_locator))
            }
        } else {
            warningDialog(requireContext(),getString(R.string.please_select_from_sub_inventory))
        }
    }


//    override fun onStatus(p0: StatusData?) {
//        barcodeReader.onStatus(p0)
//    }

    private fun isReadyToSave(qty:String,date:String):Boolean{
        var isReady = true
        if (itemData==null){
            isReady = false
            binding.itemCode.error = getString(R.string.please_scan_or_enter_item_code)
        }
        if (selectedSubInventoryCodeFrom==null){
            isReady = false
            binding.subInventoryFrom.error = getString(R.string.please_select_from_sub_inventory)
        }
        if (selectedSubInventoryCodeTo==null){
            isReady = false
            binding.subInventoryTo.error = getString(R.string.please_select_to_sub_inventory)
        }
        if (selectedLocatorCodeFrom==null){
            isReady = false
            binding.locatorFrom.error = getString(R.string.please_select_from_locator)
        }
        if (selectedlot==null){
            isReady = false
            binding.lot.error = getString(R.string.please_select_lot)
        }
        if (date.isEmpty()){
            isReady = false
            binding.date.error = getString(R.string.please_select_date)
        }
        if (qty.isEmpty()){
            isReady = false
            binding.transferQty.error = getString(R.string.please_enter_qty)
        } else {
            try{
                if (qty.toDouble()>itemData[0].onhand!!){
                    isReady = false
                    binding.transferQty.error =
                        getString(R.string.transfer_quantity_must_be_less_than_or_equal_to)+itemData[0].onhand!!
                }
            } catch (ex:Exception){
                isReady = false
                binding.transferQty.error = getString(R.string.please_enter_valid_qty)
            }
        }
        return isReady
    }

    override fun onDataScanned(data: String) {
        val scannedText = data
        onBarcodeReadData(scannedText)
    }
}