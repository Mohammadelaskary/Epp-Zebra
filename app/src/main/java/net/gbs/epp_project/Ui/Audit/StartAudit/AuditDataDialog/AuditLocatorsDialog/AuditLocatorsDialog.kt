package net.gbs.epp_project.Ui.Audit.StartAudit.AuditDataDialog.AuditLocatorsDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.Model.AuditOrderSubinventory
import net.gbs.epp_project.databinding.AuditLocatorsDialogBinding

class AuditLocatorsDialog(private val context: Context):Dialog(context) {
    var locatorsList:List<AuditOrderSubinventory> = listOf()
    private lateinit var binding:AuditLocatorsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuditLocatorsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        binding.close.setOnClickListener {
            dismiss()
        }
    }
    private lateinit var locatorsAdapter: AuditLocatorsAdapter
    private fun setUpRecyclerView() {
        locatorsAdapter = AuditLocatorsAdapter()
        binding.locatorsList.adapter = locatorsAdapter
    }

    override fun onStart() {
        super.onStart()
        locatorsAdapter.locatorsList = locatorsList
    }
}