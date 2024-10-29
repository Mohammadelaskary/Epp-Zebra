package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.ReceivePO

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.google.gson.Gson
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.PURCHASE_ORDER_KEY
import net.gbs.epp_project.Model.PurchaseOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.FragmentReceivePOBinding

class ReceivePOFragment : BaseFragmentWithViewModel<ReceivePOViewModel, FragmentReceivePOBinding>(),View.OnClickListener {
    companion object {
        fun newInstance() = ReceivePOFragment()
    }

    private var purchaseOrder: PurchaseOrder?=null
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReceivePOBinding
        get() = FragmentReceivePOBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachButtonsToListener()
        binding.poNumber.setEndIconOnClickListener {
            val poNumber = binding.poNumber.editText?.text
            if (poNumber!!.isNotEmpty()){
                viewModel.getPurchaseOrdersList(poNumber.toString())
            } else {
                binding.poNumber.error = getString(R.string.please_enter_po_number)
            }
        }

        observeData()
        clearInputLayoutError(binding.poNumber,binding.poHeaderOperatingUnit)
        setUpOperatingUnit()
    }
    var selectedPurchaseOrder:PurchaseOrder? = null
//    lateinit var operatingUnitsAdapter: ArrayAdapter<PurchaseOrder>
    var purchaseOrderList:List<PurchaseOrder> = listOf()
    private fun setUpOperatingUnit() {
//        operatingUnitsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,purchaseOrderList)
//        binding.operatingUnitSpinner.setAdapter(operatingUnitsAdapter)
//        binding.operatingUnitSpinner.setOnItemClickListener(){adapterView: AdapterView<*>, view2: View, position: Int, l: Long ->
//            selectedPurchaseOrder = purchaseOrderList[position]
//        }
    }


    private fun observeData(){
        viewModel.purchaseOrderStatus.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                else -> {
                    loadingDialog.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.purchaseOrderLiveData.observe(viewLifecycleOwner) {
            purchaseOrderList = it
//            operatingUnitsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,purchaseOrderList)
//            binding.operatingUnitSpinner.setAdapter(operatingUnitsAdapter)
            fillData(purchaseOrderList[0])
        }
    }

    private fun fillData(purchaseOrder: PurchaseOrder?) {
        binding.dataGroup.visibility = VISIBLE
        binding.poHeaderData.vendor.text = purchaseOrder?.supplier
        binding.poHeaderData.date.text = purchaseOrder?.creationDate?.substring(0,10)
        binding.poHeaderData.poNumber.text = purchaseOrder?.purchaseOrderNumber
        binding.poHeaderData.poCreatorName.text = purchaseOrder?.poCreatedUser.toString()
        binding.poHeaderData.poType.text = purchaseOrder?.potype
        binding.poHeaderData.poCreatorName.text = purchaseOrder?.poCreatedUser
        binding.poHeaderOperatingUnit.editText?.setText(purchaseOrder?.operatingunit!!)
        selectedPurchaseOrder = purchaseOrder
    }

    private fun attachButtonsToListener() {
        binding.startReceiving.setOnClickListener(this)
        binding.itemsList.setOnClickListener(this)
    }



    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.purchase_orders_list), requireActivity())
        if (viewModel.poNumber!=null){
            binding.poNumber.editText?.setText(viewModel.poNumber)
            viewModel.getPurchaseOrdersList(viewModel.poNumber!!)
        }
    }
    private var bundle = Bundle()
    override fun onClick(p0: View) {
        val poNum  = binding.poNumber.editText?.text.toString().trim()
        when(p0.id){
            R.id.search -> {
                    if (poNum.isNotEmpty()){
                        viewModel.getPurchaseOrdersList(poNum)
                    } else {
                        binding.poNumber.error = getString(R.string.please_enter_po_number)
                    }
            }
            R.id.start_receiving ->{
                if (selectedPurchaseOrder!=null) {
                    bundle.putString(
                        PURCHASE_ORDER_KEY,
                        Gson().toJson(selectedPurchaseOrder).toString()
                    )
                    view?.findNavController()
                        ?.navigate(R.id.action_receivePOFragment_to_startReceiveFragment, bundle)
                } else {
                    binding.poHeaderOperatingUnit.error =
                        getString(R.string.please_select_operating_unit)
                }
            }
            R.id.items_list ->{
                if (selectedPurchaseOrder!=null) {
                    Log.d(TAG, "onClickHeaderId: ${selectedPurchaseOrder!!.poHeaderId}")
                    Log.d(TAG, "onClickpoNumber: ${selectedPurchaseOrder!!.purchaseOrderNumber}")
                    bundle.putString(
                        PURCHASE_ORDER_KEY,
                        Gson().toJson(selectedPurchaseOrder).toString()
                    )
                    view?.findNavController()
                        ?.navigate(R.id.action_receivePOFragment_to_PODetailsFragment, bundle)
                } else {
                    binding.poHeaderOperatingUnit.error =
                        getString(R.string.please_select_operating_unit)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val poNumber        = binding.poNumber.editText?.text.toString().trim()
        if (purchaseOrder!=null){
            viewModel.poNumber = poNumber
        }
    }
}

