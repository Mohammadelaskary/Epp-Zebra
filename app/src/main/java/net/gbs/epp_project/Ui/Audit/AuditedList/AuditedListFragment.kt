package net.gbs.epp_project.Ui.Audit.AuditedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Model.AuditOrder
import net.gbs.epp_project.Model.NavigationKeys.AUDIT_ORDER_KEY
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.databinding.FragmentAuditedListBinding

class AuditedListFragment : BaseFragmentWithViewModel<AuditedListViewModel,FragmentAuditedListBinding>() {

    companion object {
        fun newInstance() = AuditedListFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAuditedListBinding
        get() = FragmentAuditedListBinding::inflate
    private var auditOrder:AuditOrder?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auditOrder = AuditOrder.fromJson(arguments?.getString(AUDIT_ORDER_KEY)!!)
        setUpRecyclerView()
        binding.search.editText?.addTextChangedListener { input ->
            transactionsAdapter.filter.filter(input)
        }
        observeGettingTransactions()
    }

    private fun observeGettingTransactions() {
        viewModel.getAuditTransactionsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING ->{
                    loadingDialog.show()
                    binding.auditOrdersList.visibility = GONE
                    binding.noData.visibility = GONE
                }
                Status.SUCCESS -> loadingDialog.dismiss()
                else -> {
                    loadingDialog.dismiss()
                    binding.auditOrdersList.visibility = GONE
                    binding.noData.visibility = VISIBLE
                    binding.noData.text = it.message
                }
            }
        }
        viewModel.getAuditTransactionsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()) {
                binding.auditOrdersList.visibility = VISIBLE
                binding.noData.visibility = GONE
                transactionsAdapter.fullTransactionsList = it
            } else {
                binding.auditOrdersList.visibility =GONE
                binding.noData.visibility = VISIBLE
                binding.noData.text = getString(R.string.no_transactions_found)
            }
        }
    }

    lateinit var transactionsAdapter: AuditedListAdapter
    private fun setUpRecyclerView() {
        transactionsAdapter = AuditedListAdapter()
        binding.auditOrdersList.adapter = transactionsAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTransactionsList(auditOrder?.physicalInventoryHeaderId!!)
        Tools.changeFragmentTitle(getString(R.string.audited_list), requireActivity())
    }
}