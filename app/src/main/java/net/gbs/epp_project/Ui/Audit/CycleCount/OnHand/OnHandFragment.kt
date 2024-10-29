package net.gbs.epp_project.Ui.Audit.CycleCount.OnHand

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Model.ItemCompare
import net.gbs.epp_project.Model.NavigationKeys.CYCLE_COUNT_HEADER_KEY
import net.gbs.epp_project.Model.NavigationKeys.ORGANIZATION_CODE_KEY
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.databinding.FragmentOnHandBinding

class OnHandFragment : BaseFragmentWithViewModel<OnHandViewModel, FragmentOnHandBinding>() {

    companion object {
        fun newInstance() = OnHandFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnHandBinding
        get() = FragmentOnHandBinding::inflate
    private var orgCode: String? = null
    private var headerId: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getInt(CYCLE_COUNT_HEADER_KEY)!=null) {
            headerId = arguments?.getInt(CYCLE_COUNT_HEADER_KEY)!!
        }
        if (arguments?.getString(ORGANIZATION_CODE_KEY)!=null) {
            orgCode = arguments?.getString(ORGANIZATION_CODE_KEY)!!

        }
        Log.d(TAG, "onViewCreatedHeaderId: $headerId")
        Log.d(TAG, "onViewCreatedOrgCode: $orgCode")
        viewModel.getOnHandsItems(headerId!!,orgCode!!)
        setUpRecyclerView()
        observeOnHandList()

    }
    private var  onHandList:List<ItemCompare> = listOf()
    private lateinit var itemsAdapter : ItemsAdapter
    private fun setUpRecyclerView() {
        itemsAdapter = ItemsAdapter(requireContext())
        binding.itemsList.adapter = itemsAdapter
    }

    private fun observeOnHandList() {
        viewModel.getOnHandsItemsStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING ->{
                    loadingDialog.show()
                }
                Status.SUCCESS ->{
                    loadingDialog.hide()
                    binding.errorMessage.visibility = GONE
                }
                else -> {
                    loadingDialog.hide()
                    binding.errorMessage.visibility = VISIBLE
                    binding.errorMessage.text = it.message
                    binding.itemsList.visibility = GONE
                }
            }
        }
        viewModel.getOnHandsItemsLiveData.observe(requireActivity()){
            onHandList = it
            if (onHandList.isNotEmpty()){
                itemsAdapter.itemsList = onHandList
            } else {
                binding.errorMessage.visibility = VISIBLE
                binding.errorMessage.text = getString(R.string.no_items_found)
                binding.itemsList.visibility = GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.on_hand),requireActivity())
    }
}