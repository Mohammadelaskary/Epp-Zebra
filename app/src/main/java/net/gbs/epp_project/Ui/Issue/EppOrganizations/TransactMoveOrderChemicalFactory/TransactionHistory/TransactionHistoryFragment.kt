package net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.TransactionHistory

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.FACTORY
import net.gbs.epp_project.Base.BundleKeys.FINAL_PRODUCT
import net.gbs.epp_project.Base.BundleKeys.INDIRECT_CHEMICALS
import net.gbs.epp_project.Base.BundleKeys.MOVE_ORDER_LINE_KEY
import net.gbs.epp_project.Base.BundleKeys.MOVE_ORDER_NUMBER_KEY
import net.gbs.epp_project.Base.BundleKeys.ORGANIZATION_ID_KEY
import net.gbs.epp_project.Base.BundleKeys.SOURCE_KEY
import net.gbs.epp_project.Model.ApiRequestBody.TransactItemsBody
import net.gbs.epp_project.Model.Lot
import net.gbs.epp_project.Model.LotQty
import net.gbs.epp_project.Model.MoveOrderLine
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.back
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.containsOnlyDigits
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.Tools.showSuccessAlerter
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.FragmentTransactionHistoryBinding

class TransactionHistoryFragment : BaseFragmentWithViewModel<TransactionHistoryViewModel,FragmentTransactionHistoryBinding>() {

    companion object {
        fun newInstance() = TransactionHistoryFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTransactionHistoryBinding
        get() = FragmentTransactionHistoryBinding::inflate

    private lateinit var moveOrderLine: MoveOrderLine
    private var moveOrderNumber : String = ""
    private var orgId : Int = -1
    private var source: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveOrderLine = MoveOrderLine.fromJson(arguments?.getString(MOVE_ORDER_LINE_KEY)!!)
        moveOrderNumber = arguments?.getString(MOVE_ORDER_NUMBER_KEY)!!
        orgId           = arguments?.getInt(ORGANIZATION_ID_KEY)!!
        source          = arguments?.getString(SOURCE_KEY)
        remainingQty    = moveOrderLine.allocatedQUANTITY!!
        when(source) {
            FACTORY, FINAL_PRODUCT -> binding.moveOrderNumberLabel.text = getString(R.string.move_order_number)
            INDIRECT_CHEMICALS     -> binding.moveOrderNumberLabel.text = getString(R.string.work_order_number)
        }
        fillMoveOrderLineData()
        clearInputLayoutError(binding.lotQty,binding.lotNumber)

        observeGettingLotList()
        setUpLotSpinner()
        setUpLotQtyRecyclerView()

        binding.save.setOnClickListener {
            if (lotQtyList.isNotEmpty()){
                if (remainingQty==0.0) {
                    viewModel.transactItems(
                        TransactItemsBody(
                            orgId = orgId,
                            lineId = moveOrderLine.linEID,
                            lineNumber = moveOrderLine.linENUMBER,
                            lots = lotQtyList,
                            transaction_date = viewModel.getTodayFullDate()
                        )
                    )
                } else {
                    warningDialog(requireContext(),
                        getString(R.string.please_add_all_quantity_to_lots))
                }
            } else {
                binding.lotNumber.error = getString(R.string.please_select_lot)
            }
        }

        binding.add.setOnClickListener {
            val enteredQty = getEditTextText(binding.lotQty)
            if (selectedLot!=null) {
                if (validQty(enteredQty)) {
                    lotQtyList.add(
                        LotQty(
                            lotName = selectedLot?.lotName,
                            qty = enteredQty.toDouble()
                        )
                    )
                    lotQtyAdapter.notifyDataSetChanged()
                    remainingQty -= enteredQty.toDouble()
                    binding.remainingQty.text = remainingQty.toString()
                    clearLotData()
                }
            }else binding.lotNumber.error = getString(R.string.please_select_lot)
        }

        observeSavingTransacting()
    }

    private fun clearLotData() {
        binding.lotQty.editText?.setText("")
        binding.lotNumberSpinner.setText("",false)
    }

    private lateinit var lotQtyAdapter: LotQtyAdapter
    private fun setUpLotQtyRecyclerView() {
        lotQtyAdapter = LotQtyAdapter(lotQtyList,object:LotQtyAdapter.OnLotQtyRemoveItemButtonClicked{
            override fun onLotQtyRemoveItemButtonClicked(position: Int) {
                remainingQty +=lotQtyList[position].qty!!
                binding.remainingQty.text = remainingQty.toString()
                selectedLot = null
                lotQtyList.removeAt(position)
                lotQtyAdapter.notifyDataSetChanged()

            }

        })
        binding.lotQtyList.adapter = lotQtyAdapter
    }

    private fun observeSavingTransacting() {
        viewModel.transactItemsStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> {
                    loadingDialog.hide()
                    showSuccessAlerter(it.message,requireActivity())
                    back(this)
                }
                else -> {
                    loadingDialog.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
    }

    private var lotList = listOf<Lot>()
    private lateinit var lotAdapter: ArrayAdapter<Lot>
    private var lotQtyList = mutableListOf<LotQty>()
    private var selectedLot: Lot? = null
    private fun setUpLotSpinner() {
        binding.lotNumberSpinner.setOnItemClickListener { _, _, selectedPosition, _ ->
//            val qtyText = getEditTextText(binding.lotQty)
//            if (validQty(qtyText)){
//                val lotQty = LotQty(
//                    lotName = lotList[selectedPosition].lotName,
//                    qty     = qtyText.toDouble()
//                )
//                lotQtyList.add(lotQty)
//                remainingQty=remainingQty-qtyText.toDouble()
//                binding.lotQty.editText?.setText(remainingQty.toString())
//                Log.d(TAG, "setUpLotSpinner: $remainingQty")
//                lotQtyAdapter.notifyDataSetChanged()
                selectedLot = lotList[selectedPosition]
                binding.lotQty.editText?.setText("$remainingQty")
        }
    }
    private var remainingQty = 0.0
    private fun validQty(qtyText: String): Boolean {
        var valid = true
        if (qtyText.isEmpty()){
            valid = false
            binding.lotQty.error = getString(R.string.please_enter_qty)
        } else {
            try {
                if(qtyText.toDouble()> remainingQty){
                    valid = false
                    binding.lotQty.error =
                        getString(R.string.lot_quantity_must_be_more_than_or_equal_to)+remainingQty

                }
                if (qtyText.toDouble()<=0){
                    valid = false
                    binding.lotQty.error =
                        getString(R.string.lot_quantity_must_not_be_equal_to_zero)
                    clearLotData()
                }
//                if (qtyText.toDouble()>selectedLot?.transactioNQUANTITY!!){
//                    valid = false
//                    binding.lotQty.error =
//                        getString(R.string.lot_quantity_must_not_be_more_than_selected_lot_quantity)
//                }
            } catch (ex:Exception){
                valid = false
                binding.lotQty.error = getString(R.string.please_enter_valid_qty)
            }
        }
        return valid
    }

    private fun observeGettingLotList() {
        viewModel.getLotListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING ->{
                    loadingDialog.show()
                }
                Status.SUCCESS -> {
                    loadingDialog.hide()
                }
                else -> {
                    loadingDialog.hide()
//                    warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.getLotListLiveData.observe(requireActivity()){
            Log.d(TAG, "observeGettingLotList: ${it.size}")
            lotList = it
            lotAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,lotList)
            binding.lotNumberSpinner.setAdapter(lotAdapter)
        }
    }

    private fun fillMoveOrderLineData() {
        binding.moveOrderNumber.text = moveOrderNumber.toString()
        binding.lineNumber.text      = moveOrderLine.linENUMBER.toString()
        binding.itemDescription.text = moveOrderLine.inventorYITEMDESC
        binding.allocatedQty.text = moveOrderLine.allocatedQUANTITY.toString()
        binding.subInventoryFrom.text  = moveOrderLine.froMSUBINVENTORYCODE
        binding.locator.text           = moveOrderLine.froMLOCATORID.toString()
        binding.remainingQty.text = remainingQty.toString()
    }

    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.lot_serial), requireActivity())
        viewModel.getLotList(orgId,moveOrderLine.inventorYITEMID,null)
    }
}