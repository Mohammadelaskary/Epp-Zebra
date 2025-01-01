package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Inspection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.findNavController
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.PO_DETAILS_ITEM_2_Key
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import net.gbs.epp_project.databinding.FragmentInspectionBinding

class InspectionFragment : BaseFragmentWithViewModel<InspectionViewModel,FragmentInspectionBinding>(),View.OnClickListener,PurchaseOrdersInspectionAdapter.POInspectionItemClick {

    companion object {
        fun newInstance() = InspectionFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInspectionBinding
        get() = FragmentInspectionBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeGettingPOs()

        Tools.attachButtonsToListener(this,binding.search)
        Tools.clearInputLayoutError(binding.receiptNo,binding.receiptNo)
        if (viewModel.poNum!=null||viewModel.receiptNo!=null){
            binding.poNumber.editText?.setText(viewModel.poNum)
            binding.receiptNo.editText?.setText(viewModel.receiptNo)
            viewModel.getPurchaseOrderReceiptNoList(viewModel.poNum!!,viewModel.receiptNo!!)
        }
    }

    private fun observeGettingPOs() {
        viewModel.poDetailsItemsLiveData.observe(requireActivity()) { poDetailsItem ->
            val itemsList = mutableListOf<PODetailsItem2>()
            poDetailsItem.forEach {
                if(it.itemqtyAccepted!!<=0||it.itemqtyRejected!!<=0)
                    if (!it.isinspected.toBoolean())
                        itemsList.add(it)
            }
            if (itemsList.isNotEmpty()) {
                binding.errorMessage.visibility = GONE
                poAdapter.poList = itemsList
            } else {
                binding.errorMessage.visibility = VISIBLE
                binding.errorMessage.text = getString(R.string.no_received_pos_to_be_inspected)
                poAdapter.poList = mutableListOf()
            }
        }
        viewModel.poDetailsItemsStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING ->{
                    loadingDialog!!.show()
                    binding.errorMessage.visibility = GONE
                }
                Status.SUCCESS ->{
                    loadingDialog!!.hide()
                }
                else -> {
                    loadingDialog!!.hide()
                    binding.errorMessage.visibility = VISIBLE
                    binding.errorMessage.text = it.message
                }
            }
        }
    }

    private lateinit var poAdapter: PurchaseOrdersInspectionAdapter
    private fun setUpRecyclerView() {
        poAdapter = PurchaseOrdersInspectionAdapter(
            this
        )
        binding.poList.adapter = poAdapter
    }

    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.inspection), requireActivity())
    }

    override fun inspectionClicked(poDetailsItem2: PODetailsItem2) {
        val userOrganization = USER?.organizations?.find { it.orgId == poDetailsItem2.shipToOrganizationId }
        if (userOrganization!=null) {
            val bundle = Bundle()
            bundle.putString(PO_DETAILS_ITEM_2_Key, PODetailsItem2.toJson(poDetailsItem2))
            view?.findNavController()
                ?.navigate(R.id.action_inspectionFragment_to_startInspectionFragment, bundle)
        } else {
            warningDialog(requireContext(),
                getString(R.string.this_user_isn_t_authorized_to_inspect_purchase_order_with_this_organization))
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.search ->{
                val poNum           = binding.poNumber.editText?.text.toString().trim()
                val receiptNum      = binding.receiptNo.editText?.text.toString().trim()
                if (poNum.isNotEmpty()||receiptNum.isNotEmpty()){
                    viewModel.getPurchaseOrderReceiptNoList(poNum,receiptNum)
                } else {
                    if (poNum.isEmpty())
                        binding.poNumber.error = getString(R.string.please_enter_po_number)
                    if (receiptNum.isEmpty())
                        binding.receiptNo.error = getString(R.string.please_enter_receipt_no)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val poNum = binding.poNumber.editText?.text.toString().trim()
        val receiptNo = binding.receiptNo.editText?.text.toString().trim()
        viewModel.poNum = poNum
        viewModel.receiptNo = receiptNo
    }
}