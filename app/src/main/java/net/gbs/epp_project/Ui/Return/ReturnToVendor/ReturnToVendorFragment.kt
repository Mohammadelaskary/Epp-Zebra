package net.gbs.epp_project.Ui.Return.ReturnToVendor

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Model.ApiRequestBody.ReturnMaterialBody
import net.gbs.epp_project.Model.POItem
import net.gbs.epp_project.Model.POLineReturn
import net.gbs.epp_project.Model.PurchaseOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.attachButtonsToListener
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.datePicker
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.showSuccessAlerter
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.FragmentReturnToVendorBinding
import net.gbs.epp_project.Ui.Return.ReturnToVendor.AddItemScreen.AddItemScreenBottomSheet
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER

class ReturnToVendorFragment : BaseFragmentWithViewModel<ReturnToVendorViewModel,FragmentReturnToVendorBinding>(),
//    Scanner.DataListener, Scanner.StatusListener,
    PoLineAdapter.OnDeleteButtonClicked,
    View.OnClickListener,AddItemScreenBottomSheet.OnAddItemBottomSheetActions {

    companion object {
        fun newInstance() = ReturnToVendorFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReturnToVendorBinding
        get() = FragmentReturnToVendorBinding::inflate
    private var addPoLineBottomSheet: AddItemScreenBottomSheet? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
//    private var organizationId:Int? = null
//    private var source = ""
    private var poLines:ArrayList<POLineReturn> = arrayListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        organizationId = arguments?.getInt(ORGANIZATION_ID_KEY)

        setUpPoLinesRecyclerView()
        binding.showCalendar.setOnClickListener {
            datePicker(requireContext(),binding.returnDate).show(requireActivity().supportFragmentManager,getString(R.string.select_a_date))
        }

//        viewModel.getTodayDate()
        clearInputLayoutError(binding.poNumber,binding.returnDate)
        attachButtonsToListener(this,binding.doReturn,binding.addItem)
        onSearchButtonClicked()
        observeSearchingPoNumber()
        observeGettingPoItems()
        observeGettingLotList()
        observeReturn()
//        observeGettingDate()

    }
    private lateinit var poLineAdapter: PoLineAdapter
    private fun setUpPoLinesRecyclerView() {
        poLineAdapter = PoLineAdapter(poLines,this)
        binding.itemsList.adapter = poLineAdapter
    }


    private fun observeReturn() {
        viewModel.returnMaterialStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> {
                    loadingDialog!!.hide()
                    clearScreenData()
                    showSuccessAlerter(it.message,requireActivity())
                }
                else -> {
                    loadingDialog!!.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
    }

    private fun clearScreenData() {
        binding.poNumber.editText?.setText("")
        binding.purchaseOrderNumberDataLayout.visibility = GONE
        poLines.clear()
        poLineAdapter.notifyDataSetChanged()
    }


    private var poItemsList = arrayListOf<POItem>()
    private fun observeGettingPoItems() {
        viewModel.poItemsStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING ->{
                    loadingDialog!!.show()
                    binding.purchaseOrderNumberDataLayout.visibility = GONE
                }
                Status.SUCCESS -> {
                    loadingDialog!!.hide()
                    binding.purchaseOrderNumberDataLayout.visibility = VISIBLE
                }
                else -> {
                    loadingDialog!!.hide()
                    binding.poNumber.error = it.message
                    binding.purchaseOrderNumberDataLayout.visibility = GONE
                }
            }
        }

        viewModel.poItemsLiveData.observe(requireActivity()){
            if (it.isNotEmpty()) {
                poItemsList = it
                binding.supplier.text = purchaseOrder?.supplier
            } else
                binding.poNumber.error = getString(R.string.this_purchase_order_has_no_items)
        }
    }

    private var purchaseOrder: PurchaseOrder? = null
    private fun observeSearchingPoNumber() {
        viewModel.purchaseOrderStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    binding.poNumber.error = it.message
                }
            }
        }
        viewModel.purchaseOrderLiveData.observe(requireActivity()){
            if (it.isNotEmpty()) {
//                if (it[0].orgId==organizationId) {
                val userOrganization = USER?.organizations?.find { organization -> organization.orgId == it[0].orgId  }
                if (userOrganization!=null) {
                    purchaseOrder = it[0]
                    viewModel.getPurchaseOrderItemListReturn(purchaseOrder?.purchaseOrderNumber!!)
                } else {
                    warningDialog(requireContext(),
                        getString(R.string.this_user_is_not_authorized_to_return_on_purchase_order_with_this_organization))
                }
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
            } else {
                binding.poNumber.error = getString(R.string.please_enter_po_number)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showBackButton(requireActivity())
        Tools.changeFragmentTitle(getString(R.string.return_to_vendor), requireActivity())
        binding.poNumber.hint = getString(R.string.purchase_order_number)

        if (viewModel.purchaseOrder!=null){
            purchaseOrder = viewModel.purchaseOrder!!
            binding.purchaseOrderNumberDataLayout.visibility = VISIBLE
            binding.supplier.text = purchaseOrder?.supplier
        }

        showBackButton(requireActivity())
        binding.returnDate.editText?.setText(viewModel.getDisplayTodayDate())
    }
    private fun observeGettingLotList() {
        viewModel.getLotListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                }
            }
        }
        viewModel.getLotListLiveData.observe(requireActivity()){
            addPoLineBottomSheet?.lotList = it
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (purchaseOrder!=null){
            viewModel.purchaseOrder = purchaseOrder
        }
        viewModel.poItemsList = poItemsList
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.do_return ->{
                val returnDate = binding.returnDate.editText?.text.toString().trim()
                if (isReadyToSave(returnDate)){
                    viewModel.returnMaterial(
                        ReturnMaterialBody(
                            poHeaderId = purchaseOrder?.poHeaderId?.toInt(),
                            transactionDate = viewModel.getTodayDate(),
                            poLines = poLines
                        )
                    )
                }
            }
            R.id.add_item -> {
                addPoLineBottomSheet = AddItemScreenBottomSheet(
                    requireContext(),requireActivity(),poItemsList,this)
                addPoLineBottomSheet?.itemsList = poItemsList
                addPoLineBottomSheet?.show()
            }
        }
    }

    private fun isReadyToSave(returnDate:String):Boolean{
        var isReady = true
        if (poLines.isEmpty()){
            warningDialog(requireContext(), getString(R.string.please_enter_items_first))
            isReady = false
        }
        if (returnDate.isEmpty()){
            binding.returnDate.error = getString(R.string.select_a_date)
            isReady = false
        }
        return isReady
    }

    override fun OnItemSelected(item: POItem) {
        viewModel.getLotList(itemId = item.inventorYITEMID, orgId = item.shipToOrganizationId!!, subInvCode = null)
    }

    override fun OnPoLineAdded(poLineReturn: POLineReturn,position:Int) {
        poLines.add(poLineReturn)
        Log.d(TAG, "onClick: $poLines")
        poLineAdapter.notifyDataSetChanged()
        poItemsList[position].isSelected = true
        addPoLineBottomSheet = null

    }

    override fun onDeleteButtonClicked(position: Int) {
        Log.d(TAG, "OnPoLineDeleted: $poItemsList")
        poItemsList.forEachIndexed { index, poItem ->
            if (poLines[position].poLineId == poItem.poLineId) {
                poItemsList[index].isSelected = false
            }
        }
        Log.d(TAG, "OnPoLineDeleted: $poItemsList")
        poLines.removeAt(position)
        poLineAdapter.notifyDataSetChanged()

    }
}