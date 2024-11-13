package net.gbs.epp_project.Ui.Return.ReturnToVendor.ItemsDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.Model.POItem
import net.gbs.epp_project.databinding.ReturnToVendorItemsDialogBinding

class ReturnToVendorItemsDialog(private val context: Context,private val onItemSelected: ReturnToVendorItemsDialogAdapter.OnItemSelected) : Dialog(context) {
    var itemsList = listOf<POItem>()
    private lateinit var binding: ReturnToVendorItemsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReturnToVendorItemsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpItemsRecyclerView()
    }
    private lateinit var itemsAdapter:ReturnToVendorItemsDialogAdapter
    private fun setUpItemsRecyclerView() {
        itemsAdapter = ReturnToVendorItemsDialogAdapter(onItemSelected)
        binding.items.adapter = itemsAdapter
    }

    override fun onStart() {
        super.onStart()
        itemsAdapter.itemsList = itemsList
        itemsAdapter.notifyDataSetChanged()
    }
}