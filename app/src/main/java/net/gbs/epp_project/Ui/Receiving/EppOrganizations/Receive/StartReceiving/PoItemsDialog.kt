package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.StartReceiving

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.Model.PODetailsItem
import net.gbs.epp_project.databinding.PoItemsDialogBinding
import okhttp3.internal.notify

class PoItemsDialog(context: Context,onPOLineClicked: PoDetailsAdapter.OnPOLineClicked): Dialog(context) {
    var itemsList:List<PODetailsItem> = listOf()

    private val poDetailsAdapter = PoDetailsAdapter(context,onPOLineClicked)
    private lateinit var binding: PoItemsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PoItemsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        poDetailsAdapter.poList = itemsList
        binding.itemsList.adapter = poDetailsAdapter
    }

    override fun onStart() {
        super.onStart()
        poDetailsAdapter.poList=itemsList
    }
}