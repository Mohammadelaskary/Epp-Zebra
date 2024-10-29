package net.gbs.epp_project.Ui.Issue.EppOrganizations.SpareParts

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys
import net.gbs.epp_project.Base.BundleKeys.INDIRECT_CHEMICALS
import net.gbs.epp_project.Base.BundleKeys.SOURCE_KEY
import net.gbs.epp_project.Base.BundleKeys.SPARE_PARTS
import net.gbs.epp_project.Model.ApiRequestBody.TransactItemsBody
import net.gbs.epp_project.Model.Locator
import net.gbs.epp_project.Model.MoveOrderLine
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.SubInventory
import net.gbs.epp_project.Model.WorkOrderOrder
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.EditTextActionHandler
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.attachButtonsToListener
import net.gbs.epp_project.Tools.Tools.back
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.Tools.showSuccessAlerter
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Tools.ZebraScanner
import net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueReports.IssueOrderReport.IssueOrderReportDialog
import net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueReports.OnHandForIssueOrderReport.OnHandIssueOrderReportDialog
import net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.MoveOrderInfoDialog
import net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.MoveOrderLinesDialog.MoveOrderLinesAdapter
import net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.MoveOrderLinesDialog.MoveOrderLinesDialog
import net.gbs.epp_project.databinding.FragmentTransactSparePartsWorkOrderBinding

class TransactSparePartsWorkOrderFragment : BaseFragmentWithViewModel<TransactSparePartsWorkOrderViewModel, FragmentTransactSparePartsWorkOrderBinding>(),MoveOrderInfoDialog.OnInfoDialogButtonsClicked,
//    DataListener,StatusListener,
    ZebraScanner.OnDataScanned,
    OnClickListener, MoveOrderLinesAdapter.OnMoveOrderLineItemClicked {

    companion object {
        fun newInstance() = TransactSparePartsWorkOrderFragment()
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTransactSparePartsWorkOrderBinding
        get() = FragmentTransactSparePartsWorkOrderBinding::inflate

    private lateinit var barcodeReader :ZebraScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private var orgId: Int = -1
    private var source:String? =null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcodeReader = ZebraScanner(requireActivity(),this)
        orgId = arguments?.getInt(BundleKeys.ORGANIZATION_ID_KEY)!!
        source = arguments?.getString(SOURCE_KEY)
        setUpMoveOrdersNumbersSpinner()
        observeGettingIssueOrderLists()
        observeGettingWorkOrdersList()
        setUpReportsDialogs()



//        binding.issueTypeGroup.setOnCheckedChangeListener { radioGroup, id ->
//            when(id){
//                R.id.allocate_only -> {
//                    binding.allocateGroup.visibility = VISIBLE
//                    binding.transact.visibility = GONE
//                    binding.lotSerial.visibility = GONE
//                }
//                R.id.transact_only ->{
//                    binding.allocateGroup.visibility = GONE
//                    binding.transact.visibility = VISIBLE
//                    binding.lotSerial.visibility = VISIBLE
//                }
//            }
//        }




        clearInputLayoutError(binding.workOrderNumber, binding.subInventoryTo, binding.itemCode)
        attachButtonsToListener(
            this,
            binding.info,
            binding.transact,
            binding.showLinesListDialog,
            binding.showLinesListDialog,
            binding.clearItem!!,
            binding.lotSerial
        )

//        observeGettingMoveOrder()
        observeGettingMoveOrderLines()
        observeGettingSubInventoryList()
        observeGettingLocatorsList()
        observeAllocatingTransactingItems()
        setUpSubInventorySpinner()
        setUpLocatorsSpinner()


        EditTextActionHandler.OnEnterKeyPressed(binding.itemCode) {
            val itemCode = getEditTextText(binding.itemCode)
            if (moveOrdersLines.isNotEmpty()) {
                scannedItem = moveOrdersLines.find { it.inventorYITEMCODE == itemCode }
                if (scannedItem != null) {
                    fillItemData(scannedItem!!)
                } else {
                    binding.onScanItemViewsGroup.visibility = GONE
                    binding.itemCode.error = getString(R.string.wrong_item_code)
                }
            } else {
                binding.onScanItemViewsGroup.visibility = GONE
                warningDialog(
                    requireContext(),
                    getString(R.string.please_enter_valid_work_order_number)
                )

            }
        }
    }


    private lateinit var ordersItemsDialog: IssueOrderReportDialog
    private lateinit var onHandItemsDialog: OnHandIssueOrderReportDialog
    private lateinit var moveOrderInfoDialog: MoveOrderInfoDialog
    private lateinit var linesDialog: MoveOrderLinesDialog
    private fun setUpReportsDialogs() {
        ordersItemsDialog   = IssueOrderReportDialog(requireContext())
        onHandItemsDialog   = OnHandIssueOrderReportDialog(requireContext())
        moveOrderInfoDialog = MoveOrderInfoDialog(requireContext(), this)
        linesDialog         = MoveOrderLinesDialog(requireContext(),this)
    }

    private fun observeGettingIssueOrderLists() {
        viewModel.getIssueOrdersListStatus.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    binding.issueOrdersListsLoading?.show()
                    binding.info.visibility = GONE
                }
                Status.SUCCESS -> {
                    binding.issueOrdersListsLoading?.hide()
                    binding.info.visibility = VISIBLE
                }
                else -> {
                    binding.issueOrdersListsLoading?.hide()
                    Tools.warningDialog(requireContext(), it.message)
                    binding.info.visibility = GONE
                }
            }
        }
        viewModel.getIssueOrdersListLiveData.observe(requireActivity()) {
            ordersItemsDialog.items = it.getMoveOrderLinesList
            onHandItemsDialog.items = it.getOnHandList
            binding.info.isEnabled = true
        }
    }

    private var workOrdersList = mutableListOf<WorkOrderOrder>()
    private lateinit var moveOrdersAdapter: ArrayAdapter<WorkOrderOrder>
    private fun setUpMoveOrdersNumbersSpinner() {
        binding.workOrderNumberSpinner?.setOnItemClickListener { _, _, position, _ ->
            selectedMoveOrder = workOrdersList[position]
            binding.info.isEnabled = false
            viewModel.getIssueOrderLists(selectedMoveOrder?.moveOrderRequestNumber!!, orgId)
            viewModel.getMoveOrderLines(selectedMoveOrder?.workOrderName!!, orgId)
            binding.itemCode.editText?.setText("")
            binding.onScanItemViewsGroup.visibility = GONE
        }
    }

    private fun observeGettingWorkOrdersList() {
        viewModel.getWorkOrdersListStatus.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                else -> {
                    warningDialog(requireContext(), it.message)
                    loadingDialog.hide()
                }
            }
        }
        viewModel.getWorkOrdersListLiveData.observe(requireActivity()) {
            it.forEach { workOrderOrder ->
                val workOrder = workOrdersList.find { it.workOrderName==workOrderOrder.workOrderName }
                if (workOrder==null)
                    workOrdersList.add(workOrderOrder)
            }
            moveOrdersAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, workOrdersList)
            binding.workOrderNumberSpinner.setAdapter(moveOrdersAdapter)
            if (workOrdersList.isNotEmpty()) {
                if (selectedMoveOrder != null) {
                    val workOrder =
                        workOrdersList.find { it.workOrderName == selectedMoveOrder?.workOrderName }
                    if (workOrder != null) {
                        refillMoveOrderData()
                    }
                }
            } else {
                back(this)
            }
        }
    }

    private fun refillMoveOrderData() {
            binding.info.isEnabled = false
            viewModel.getIssueOrderLists(selectedMoveOrder?.moveOrderRequestNumber!!, orgId)
            viewModel.getMoveOrderLines(selectedMoveOrder?.workOrderName!!, orgId)
            binding.itemCode.editText?.setText("")
            binding.onScanItemViewsGroup.visibility = GONE
    }



    private fun observeAllocatingTransactingItems() {
        viewModel.allocateItemsStatus.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> {
                    loadingDialog.hide()
                    clearLineData()
//                    back(this)
                    viewModel.getMoveOrderLines(selectedMoveOrder?.workOrderName!!, orgId)
                    viewModel.getIssueOrderLists(selectedMoveOrder?.moveOrderRequestNumber!!,orgId)
                    showSuccessAlerter(it.message, requireActivity())
                }

                else -> {
                    loadingDialog.hide()
                    warningDialog(requireContext(), it.message)
                }
            }
        }
    }

    private fun clearLineData() {
        binding.onScanItemViewsGroup.visibility = GONE
        binding.itemCode.editText?.setText("")
        binding.allocatedQty.editText?.setText("")
        binding.subInventoryFromSpinner.setText("", false)
        binding.subInventoryToSpinner.setText("", false)
        binding.locatorFromSpinner.setText("", false)
        scannedItem = null
        selectedSubInventoryCodeFrom = null
        selectedLocatorCodeFrom = null
        selectedSubInventoryCodeTo = null
    }


    private var subInventoryList = listOf<SubInventory>()
    private lateinit var subInventoryToAdapter: ArrayAdapter<SubInventory>
    private lateinit var subInventoryFromAdapter: ArrayAdapter<SubInventory>
    private var selectedSubInventoryCodeFrom: String? = null
    private var selectedSubInventoryCodeTo: String? = null
    private var subInvType: SubInvType = SubInvType.FROM
    private fun observeGettingSubInventoryList() {
        viewModel.getSubInvertoryListStatus.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                else -> {
                    loadingDialog.hide()
                    warningDialog(requireContext(), it.message)
                }
            }
        }
        viewModel.getSubInvertoryListLiveData.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                subInventoryList = it
                subInventoryFromAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    subInventoryList
                )
                binding.subInventoryFromSpinner.setAdapter(subInventoryFromAdapter)
                subInventoryToAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    subInventoryList
                )
                binding.subInventoryToSpinner.setAdapter(subInventoryToAdapter)
            } else {
                warningDialog(
                    requireContext(),
                    getString(R.string.no_sub_inventories_for_this_org_id)
                )
            }
        }
    }

    private fun setUpSubInventorySpinner() {
        binding.subInventoryFromSpinner.setOnItemClickListener { _, _, selectedIndex, _ ->
            selectedSubInventoryCodeFrom = subInventoryList[selectedIndex].subInventoryCode
            viewModel.getLocatorsList(orgId, selectedSubInventoryCodeFrom!!)
            subInvType = SubInvType.FROM
        }
        binding.subInventoryToSpinner.setOnItemClickListener { _, _, selectedIndex, _ ->
            selectedSubInventoryCodeTo = subInventoryList[selectedIndex].subInventoryCode
//            viewModel.getLocatorsList(orgId, selectedSubInventoryCodeTo!!)
            subInvType = SubInvType.To
        }
    }


    private var locatorsList = listOf<Locator>()
    private lateinit var locatorsFromAdapter: ArrayAdapter<Locator>
    private var selectedLocatorCodeFrom: String? = null

    private fun observeGettingLocatorsList() {
        viewModel.getLocatorsListStatus.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                else -> {
                    loadingDialog.hide()
//                    warningDialog(requireContext(), it.message)
                }
            }
        }
        viewModel.getLocatorsListLiveData.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                locatorsList = it

                locatorsFromAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    locatorsList
                )
                binding.locatorFromSpinner.setAdapter(locatorsFromAdapter)

            } else {
                warningDialog(
                    requireContext(),
                    getString(R.string.no_locators_for_this_sub_inventory)
                )
            }
        }
    }

    private fun setUpLocatorsSpinner() {
        binding.locatorFromSpinner.setOnItemClickListener { _, _, selectedIndex, _ ->
            selectedLocatorCodeFrom = locatorsList[selectedIndex].locatorCode
        }
    }

    private var moveOrdersLines = listOf<MoveOrderLine>()
    private fun observeGettingMoveOrderLines() {
        viewModel.getMoveOrderLinesStatus.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                else -> {
                    loadingDialog.hide()
                    back(this)
                    warningDialog(requireContext(), it.message)
                }
            }
        }
        viewModel.getMoveOrderLinesLiveData.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                moveOrdersLines = it
                linesDialog.linesList = moveOrdersLines
                binding.dataGroup.visibility = VISIBLE
            } else {
               warningDialog(
                        requireContext(),
                        getString(R.string.no_lines_for_this_job_order)
                    )

            }
        }
    }


    private var selectedMoveOrder: WorkOrderOrder? = null


    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.start_issue), requireActivity())
        Tools.showBackButton(requireActivity())
        binding.transactionDate?.editText?.setText(viewModel.getDisplayTodayDate())
        when(source){
            SPARE_PARTS -> {
                viewModel.getWorkOrdersList(orgId)
                binding.workOrderNumber.hint = getString(R.string.work_order_number)
                binding.subInventoryTo.visibility = GONE
                binding.line.visibility = GONE
                binding.transact.text = getString(R.string.issue)
            }
            INDIRECT_CHEMICALS -> {
                viewModel.getJobOrdersList(orgId)
                binding.workOrderNumber.hint = getString(R.string.job_order_number)
            }
        }

        barcodeReader.onResume()

        if (viewModel.moveOrder!=null){
            selectedMoveOrder = viewModel.moveOrder
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroy: ")
        if (selectedMoveOrder!=null){
            viewModel.moveOrder = selectedMoveOrder
        }
    }

    override fun onPause() {
        super.onPause()
        barcodeReader.onPause()
    }

    private var scannedItem: MoveOrderLine? = null
//    override fun onData(collection: ScanDataCollection) {
//        requireActivity().runOnUiThread {
//            if (moveOrdersLines.isNotEmpty()) {
//                val scannedText = barcodeReader.onData(collection)
//                scannedItem = moveOrdersLines.find { it.inventorYITEMCODE == scannedText }
//                if (scannedItem != null) {
//                    fillItemData(scannedItem!!)
//                } else {
//                    binding.onScanItemViewsGroup.visibility = GONE
//                    binding.itemCode.error = getString(R.string.wrong_item_code)
//                }
//            } else {
//                binding.onScanItemViewsGroup.visibility = GONE
//          warningDialog(
//                        requireContext(),
//                        getString(R.string.please_enter_valid_work_order_number)
//                    )
//
//            }
//           barcodeReader.restartReadData()
//        }
//    }

    private fun fillItemData(scannedItem: MoveOrderLine) {
        if (getEditTextText(binding.itemCode).isEmpty())
            binding.itemCode.editText?.setText(scannedItem.inventorYITEMCODE)
        binding.itemDesc.text = scannedItem.inventorYITEMDESC
        binding.onHandQty.text = scannedItem.onHANDQUANTITY.toString()
        binding.onScanItemViewsGroup.visibility = VISIBLE
        if (scannedItem.froMSUBINVENTORYCODE?.isNotEmpty()!!) {
            binding.subInventoryFromSpinner.setText(scannedItem.froMSUBINVENTORYCODE, false)
            binding.subInventoryFrom.isEnabled = false
            selectedSubInventoryCodeFrom = scannedItem.froMSUBINVENTORYCODE
            viewModel.getLocatorsList(orgId, scannedItem.froMSUBINVENTORYCODE!!)
        }
        if (scannedItem.tOSUBINVENTORYCODE?.isNotEmpty()!!) {
            binding.subInventoryToSpinner.setText(scannedItem.tOSUBINVENTORYCODE, false)
            selectedSubInventoryCodeTo = scannedItem.tOSUBINVENTORYCODE
            binding.subInventoryTo.isEnabled = false
        } else {
            viewModel.getSubInvertoryList(orgId)
        }
        var locatorFrom = ""
        if (scannedItem.froMLOCATORCode!!.isNotEmpty()) {
            locatorFrom = scannedItem.froMLOCATORCode.toString()
            selectedLocatorCodeFrom = scannedItem.froMLOCATORCode.toString()
            binding.locatorFrom.isEnabled = false
        }
        binding.locatorFromSpinner.setText(locatorFrom, false)
        val allocatedQty = scannedItem.quantity.toString()
//        if (allocatedQty.isNotEmpty()) {
//            binding.allocatedQty.isEnabled = false
        Log.d(TAG, "fillItemData: $source")
            binding.allocatedQty.editText?.setText(allocatedQty)
            if (source == INDIRECT_CHEMICALS) {
                binding.lotSerial.visibility = VISIBLE
                binding.transact.visibility = GONE
            } else {
                binding.lotSerial.visibility = GONE
                binding.transact.visibility = VISIBLE
            }
//        }

    }


//        if (source.equals(INDIRECT_CHEMICALS)){
////            binding.issueTypeGroup.visibility = GONE
////            binding.allocateGroup.visibility = GONE
//            binding.transact.visibility = VISIBLE
//            binding.lotSerial.visibility = VISIBLE
//        } else {
//            binding.issueTypeGroup.visibility = VISIBLE
//            binding.transact.visibility = GONE
//            binding.lotSerial.visibility = GONE
//        }


//    override fun onStatus(statusData: StatusData) {
//       barcodeReader.onStatus(statusData)
//    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.info -> moveOrderInfoDialog.show()
            R.id.transact -> {
                val body = TransactItemsBody(
                    orgId = orgId,
                    lineId = scannedItem?.linEID,
                    lineNumber = scannedItem?.linENUMBER,
                    transaction_date = viewModel.getTodayDate()
                )
                viewModel.transactItems(body)
                Log.d(TAG, "onClick: Transact")
            }
            R.id.show_lines_list_dialog -> linesDialog.show()
            R.id.clear_item -> clearLineData()
            R.id.lot_serial -> {
                val bundle = Bundle()
                bundle.putString(BundleKeys.MOVE_ORDER_NUMBER_KEY,selectedMoveOrder?.moveOrderRequestNumber!!)
                bundle.putString(BundleKeys.MOVE_ORDER_LINE_KEY,MoveOrderLine.toJson(scannedItem!!))
                bundle.putString(SOURCE_KEY,source)
                bundle.putInt(BundleKeys.ORGANIZATION_ID_KEY,orgId)
                Navigation.findNavController(requireView()).navigate(R.id.action_transactSparePartsWorkOrderFragment_to_transactionHistoryFragment,bundle)
            }
        }
    }

    private fun isReadyForShipping():Boolean{
        var isReady = true
        if (selectedSubInventoryCodeFrom==null){
            binding.subInventoryFrom.error = getString(R.string.please_select_from_sub_inventory)
            isReady = false
        }
        if (selectedLocatorCodeFrom==null){
            binding.locatorFrom.error = getString(R.string.please_select_to_locator)
            isReady = false
        }
//        else {
//            if (!containsOnlyDigits(issueQty)){
//                binding.issueQty.error = getString(R.string.please_enter_valid_qty)
//                isReady = false
//            }
//        }
        return isReady
    }
    private fun isReadyForTransaction():Boolean{
        var isReady = true
        if (selectedSubInventoryCodeFrom==null){
            binding.subInventoryFrom.error = getString(R.string.please_select_from_sub_inventory)
            isReady = false
        }
        if (selectedLocatorCodeFrom==null){
            binding.locatorFrom.error = getString(R.string.please_select_from_locator)
            isReady = false
        }
//        else {
//            if (!containsOnlyDigits(issueQty)){
//                binding.issueQty.error = getString(R.string.please_enter_valid_qty)
//                isReady = false
//            }
//        }
        if (selectedSubInventoryCodeTo==null){
            binding.subInventoryTo.error = getString(R.string.please_select_to_sub_inventory)
            isReady = false
        }
        if (locatorsList.isNotEmpty()) {
            if (selectedLocatorCodeFrom == null) {
                binding.locatorFrom.error = getString(R.string.please_select_to_locator)
                isReady = false
            }
        }
        return isReady
    }

    enum class SubInvType{
        FROM,To
    }

    override fun OnOrderItemsButtonClicked() {
        moveOrderInfoDialog.dismiss()
        ordersItemsDialog.show()
    }

    override fun OnOrderOnHandButtonClicked() {
        moveOrderInfoDialog.dismiss()
        onHandItemsDialog.show()
    }

    override fun onMoveOrderLineClicked(item: MoveOrderLine) {
        scannedItem = item
        fillItemData(scannedItem!!)
        linesDialog.dismiss()
    }

    override fun onDataScanned(data: String) {
        if (moveOrdersLines.isNotEmpty()) {
            val scannedText = data
            scannedItem = moveOrdersLines.find { it.inventorYITEMCODE == scannedText }
            if (scannedItem != null) {
                fillItemData(scannedItem!!)
            } else {
                binding.onScanItemViewsGroup.visibility = GONE
                binding.itemCode.error = getString(R.string.wrong_item_code)
            }
        } else {
            binding.onScanItemViewsGroup.visibility = GONE
            warningDialog(
                requireContext(),
                getString(R.string.please_enter_valid_work_order_number)
            )

        }
    }
}