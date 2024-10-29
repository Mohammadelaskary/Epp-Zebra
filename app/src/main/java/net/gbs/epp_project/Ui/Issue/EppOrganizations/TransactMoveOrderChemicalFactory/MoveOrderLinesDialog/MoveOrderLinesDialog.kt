package net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory.MoveOrderLinesDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.Model.MoveOrderLine
import net.gbs.epp_project.databinding.PoItemsDialogBinding

class MoveOrderLinesDialog(context: Context,onMoveOrderLineItemClicked: MoveOrderLinesAdapter.OnMoveOrderLineItemClicked):Dialog(context) {
    var linesList:List<MoveOrderLine> = listOf()

    private val moveOrderLinesAdapter = MoveOrderLinesAdapter(onMoveOrderLineItemClicked)
    private lateinit var binding: PoItemsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PoItemsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        moveOrderLinesAdapter.linesList = linesList
        binding.itemsList.adapter = moveOrderLinesAdapter
    }

    override fun onStart() {
        super.onStart()
        moveOrderLinesAdapter.linesList=linesList
    }
}