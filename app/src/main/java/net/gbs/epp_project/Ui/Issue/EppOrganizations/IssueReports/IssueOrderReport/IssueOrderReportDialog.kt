package net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueReports.IssueOrderReport

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.Model.IssueOrderReportItem
import net.gbs.epp_project.databinding.IssueOrderItemBinding
import net.gbs.epp_project.databinding.IssueReportDialogLayoutBinding

class IssueOrderReportDialog(context: Context): Dialog(context) {
    var items :List<IssueOrderReportItem> = listOf()

    private lateinit var binding: IssueReportDialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IssueReportDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpItemsRecyclerView()
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    private lateinit var adapter: IssueOrderReportAdapter
    private fun setUpItemsRecyclerView() {
        adapter = IssueOrderReportAdapter()
        binding.issueItems.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.items = items
    }
}