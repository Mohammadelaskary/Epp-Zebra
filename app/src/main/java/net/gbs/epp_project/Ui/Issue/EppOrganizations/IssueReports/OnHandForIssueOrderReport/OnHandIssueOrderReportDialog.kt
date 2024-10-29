package net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueReports.OnHandForIssueOrderReport

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.Model.IssueOrderReportItem
import net.gbs.epp_project.Model.OnHandIssueOrderItem
import net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueReports.IssueOrderReport.IssueOrderReportAdapter
import net.gbs.epp_project.databinding.IssueOrderItemBinding
import net.gbs.epp_project.databinding.IssueReportDialogLayoutBinding

class OnHandIssueOrderReportDialog(context: Context): Dialog(context) {
    var items :List<OnHandIssueOrderItem> = listOf()
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

    private lateinit var adapter: OnHandIssueOrderReportAdapter
    private fun setUpItemsRecyclerView() {
        adapter = OnHandIssueOrderReportAdapter()
        binding.issueItems.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.items = items
    }
}