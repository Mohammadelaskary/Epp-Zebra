package net.gbs.epp_project.Ui.Return.ReturnToVendor.AddItemScreen

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.gbs.epp_project.Base.BundleKeys
import net.gbs.epp_project.Model.Lot
import net.gbs.epp_project.Model.LotQty
import net.gbs.epp_project.Model.POItem
import net.gbs.epp_project.Model.POLineReturn
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.Tools.ZebraScanner
import net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.TransactionHistory.LotQtyAdapter
import net.gbs.epp_project.Ui.Return.ReturnToVendor.ItemsDialog.ReturnToVendorItemsDialog
import net.gbs.epp_project.Ui.Return.ReturnToVendor.ItemsDialog.ReturnToVendorItemsDialogAdapter
import net.gbs.epp_project.databinding.FragmentAddItemScreenBinding

class AddItemScreenBottomSheet(private val context: Context,private val activity: Activity,var itemsList:MutableList<POItem>, val onAddItemBottomSheetActions: OnAddItemBottomSheetActions) : OnClickListener,ZebraScanner.OnDataScanned,ReturnToVendorItemsDialogAdapter.OnItemSelected,BottomSheetDialog(context),LotQtyAdapter.OnLotQtyRemoveItemButtonClicked {

    private lateinit var binding: FragmentAddItemScreenBinding
    private lateinit var barcodeReader: ZebraScanner
    private lateinit var itemsDialog: ReturnToVendorItemsDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddItemScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        barcodeReader = ZebraScanner(activity,this)
        itemsDialog = ReturnToVendorItemsDialog(context,this)
        itemsDialog.itemsList = itemsList
        setUpLotSpinner()
        setUpLotQtyRecyclerView()
        Tools.attachButtonsToListener(this, binding.save,binding.showLinesListDialog)
        clearInputLayoutError(binding.itemCode,binding.lotQty,binding.lotNumber)
    }

    private lateinit var lotQtyAdapter: LotQtyAdapter
    private fun setUpLotQtyRecyclerView() {
        lotQtyAdapter = LotQtyAdapter(lotQtyList,this)
        binding.lotQtyList.adapter = lotQtyAdapter
    }


    private lateinit var lotAdapter : ArrayAdapter<Lot>
    var lotList : List<Lot> = listOf()
        set(value) {
            field = value
            lotAdapter = ArrayAdapter(context,android.R.layout.simple_list_item_1,value)
            binding.lotNumberSpinner.setAdapter(lotAdapter)
        }
    private var lotQtyList  = ArrayList<LotQty>()
    private fun setUpLotSpinner() {
        binding.lotNumberSpinner.setOnItemClickListener { adapterView, view, selectedPosition, l ->
            if (Tools.getEditTextText(binding.lotQty).isNotEmpty()){
                try {
                    val lotQty = LotQty(
                        lotName = lotList[selectedPosition].lotName,
                        qty = Tools.getEditTextText(binding.lotQty).toDouble()
                    )
                    lotQtyList.add(lotQty)
                    lotQtyAdapter.notifyItemInserted(lotQtyList.size-1)
                    binding.lotQty.editText?.setText("")
                    binding.lotNumberSpinner.setText("",false)
                } catch (ex:Exception){
                    binding.lotQty.error = context.getString(R.string.please_enter_valid_qty)
                }
            } else {
                binding.lotQty.error = context.getString(R.string.please_enter_qty)
            }
        }
    }

    var scannedItemsList = mutableListOf<POItem>()
    override fun onDataScanned(data: String) {
        itemsList.forEachIndexed { index, poItem ->
            if (poItem.itemcode == data) {
                scannedItemsList.add(poItem)
                selectedItemPosition = index
            }
        }
        Log.d(TAG, "onDataScannedData: $data")
        Log.d(TAG, "onDataScannedItemsList: $itemsList")
        Log.d(TAG, "onDataScannedScannedItemsList: $scannedItemsList")
        if (scannedItemsList.size==1) {
            if (!scannedItemsList[0].isSelected) {
                scannedItem = scannedItemsList[0]
                fillItemData()
                onAddItemBottomSheetActions.OnItemSelected(scannedItem!!)
            } else {
                Toast.makeText(context,context.getString(R.string.added_before),Toast.LENGTH_SHORT).show()
            }
        } else {
            itemsDialog.itemsList = scannedItemsList
            itemsDialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        barcodeReader.onResume()
        clearData()
        Tools.changeFragmentTitle(context.getString(R.string.add_item),activity)

    }

    private fun clearData() {
        binding.itemCode.editText?.setText("")
        binding.itemDataGroup.visibility = GONE
        binding.lotQty.editText?.setText("")
        binding.lotNumberSpinner.setText("",false)
        lotQtyList.clear()
        lotQtyAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        barcodeReader.onPause()
        scannedItemsList.clear()
    }
    private var scannedItem :POItem? = null
    private var selectedItemPosition:Int = -1
    override fun onItemSelected(item: POItem,position: Int) {
        if (!item.isSelected) {
            itemsDialog.dismiss()
            scannedItem = item
            selectedItemPosition = position
            Log.d(TAG, "onItemSelected: $selectedItemPosition")
            fillItemData()
        } else {
            Toast.makeText(context,context.getString(R.string.added_before),Toast.LENGTH_SHORT).show()
        }

    }



    private fun fillItemData() {
        binding.itemDataGroup.visibility = View.VISIBLE
        binding.itemCode.editText?.setText(scannedItem?.itemcode)
        binding.itemDesc.text = scannedItem?.itemdesc
        if (scannedItem?.mustHaveLot()!!){
            onAddItemBottomSheetActions.OnItemSelected(scannedItem!!)
            binding.lotNumber.visibility = VISIBLE
        } else {
            binding.lotQty.hint = context.getString(R.string.return_quantity)
            binding.lotNumber.visibility = GONE
        }
    }

    override fun onLotQtyRemoveItemButtonClicked(position: Int) {
        lotQtyList.removeAt(position)
        lotQtyAdapter.notifyItemRemoved(position)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.show_lines_list_dialog ->{
                itemsDialog.itemsList
                itemsDialog.show()
            }
            R.id.save -> {
                if (isReadyToAdd()){
                    if (scannedItem?.mustHaveLot()!!) {
                        val poLineReturn1 = POLineReturn(
                            transactioNID = scannedItem?.transactioNID,
                            transactioNTYPE = scannedItem?.transactioNTYPE,
                            shipToOrganizationId = scannedItem?.shipToOrganizationId,
                            receiptNum = scannedItem?.receiptno,
                            poLineId = scannedItem?.poLineId,
                            itemDescription = scannedItem?.itemdesc,
                            lots = lotQtyList,
                            quantityReturned = calculateReturnQty()
                        )
                        onAddItemBottomSheetActions.OnPoLineAdded(
                            poLineReturn1,
                            selectedItemPosition
                        )
                    } else {

                        val poLineReturn2 =POLineReturn(
                            transactioNID = scannedItem?.transactioNID,
                            transactioNTYPE = scannedItem?.transactioNTYPE,
                            shipToOrganizationId = scannedItem?.shipToOrganizationId,
                            receiptNum = scannedItem?.receiptno,
                            poLineId = scannedItem?.poLineId,
                            itemDescription = scannedItem?.itemdesc,
                            lots = arrayListOf(),
                            quantityReturned = getEditTextText(binding.lotQty).toDouble()
                        )
                        onAddItemBottomSheetActions.OnPoLineAdded(
                            poLineReturn2,
                            selectedItemPosition
                        )
                    }
                    dismiss()
                }
            }
        }
    }

    private fun calculateReturnQty(): Double {
        var sum = 0.0
        lotQtyList.forEach {
            sum += it.qty!!
        }
        return sum
    }

    private fun isReadyToAdd(): Boolean {
        var isReady = true
        if (scannedItem==null){
            isReady = false
            binding.itemCode.error = context.getString(R.string.please_scan_or_enter_item_code)
        }
        if (scannedItem?.mustHaveLot()!!){
            if (lotQtyList.isEmpty()){
                isReady = false
                Tools.warningDialog(context, context.getString(R.string.please_select_lot))
            }
        } else {
            val returnQty = getEditTextText(binding.lotQty)
            if (returnQty.isEmpty()){
                isReady = false
                binding.lotQty.error = context.getString(R.string.please_enter_return_quantity)
            }
        }
        return isReady
    }

    interface OnAddItemBottomSheetActions{
        fun OnItemSelected(item: POItem)
        fun OnPoLineAdded(poLineReturn: POLineReturn,position: Int)
    }
}