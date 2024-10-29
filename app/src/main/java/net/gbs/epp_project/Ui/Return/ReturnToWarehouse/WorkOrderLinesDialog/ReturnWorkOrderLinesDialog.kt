package net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.MoveOrderLinesDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.Model.MoveOrderLine
import net.gbs.epp_project.Model.ReturnWorkOrderLine
import net.gbs.epp_project.databinding.PoItemsDialogBinding

class ReturnWorkOrderLinesDialog(context: Context, onMoveOrderLineItemClicked: ReturnWorkOrderLinesAdapter.OnWorkOrderLineItemClicked):Dialog(context) {
    var linesList:List<ReturnWorkOrderLine> = listOf()

    private val returnWorkOrderLinesAdapter = ReturnWorkOrderLinesAdapter(onMoveOrderLineItemClicked)
    private lateinit var binding: PoItemsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PoItemsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        returnWorkOrderLinesAdapter.linesList = linesList
        binding.itemsList.adapter = returnWorkOrderLinesAdapter
    }

    override fun onStart() {
        super.onStart()
        returnWorkOrderLinesAdapter.linesList=linesList
    }
}