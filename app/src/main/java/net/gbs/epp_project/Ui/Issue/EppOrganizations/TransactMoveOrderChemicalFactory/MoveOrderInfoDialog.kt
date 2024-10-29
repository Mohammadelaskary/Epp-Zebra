package net.gbs.epp_project.Ui.Issue.EppOrganizations.TransactMoveOrderChemicalFactory

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.databinding.MoveOrderInfoDialogLayoutBinding

class MoveOrderInfoDialog(context: Context,val onInfoDialogButtonsClicked: OnInfoDialogButtonsClicked): Dialog(context) {

    private lateinit var binding: MoveOrderInfoDialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MoveOrderInfoDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.orderItems.setOnClickListener { onInfoDialogButtonsClicked.OnOrderItemsButtonClicked() }
        binding.onHandItems.setOnClickListener { onInfoDialogButtonsClicked.OnOrderOnHandButtonClicked() }
    }
    interface OnInfoDialogButtonsClicked {
        fun OnOrderItemsButtonClicked()
        fun OnOrderOnHandButtonClicked()
    }
}