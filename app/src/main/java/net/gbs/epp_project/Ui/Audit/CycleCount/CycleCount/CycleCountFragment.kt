package net.gbs.epp_project.Ui.Audit.CycleCount.CycleCount

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation

import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Model.CycleCountHeader
import net.gbs.epp_project.Model.Item
import net.gbs.epp_project.Model.LocatorAudit
import net.gbs.epp_project.Model.NavigationKeys.CYCLE_COUNT_HEADER_KEY
import net.gbs.epp_project.Model.NavigationKeys.ITEM_KEY
import net.gbs.epp_project.Model.NavigationKeys.LOCATOR_KEY
import net.gbs.epp_project.Model.NavigationKeys.ORGANIZATION_KEY
import net.gbs.epp_project.Model.OrganizationAudit
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.attachButtonsToListener
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.showErrorAlerter
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Tools.ZebraScanner
import net.gbs.epp_project.databinding.FragmentCycleCountBinding

class CycleCountFragment : BaseFragmentWithViewModel<CycleCountViewModel,FragmentCycleCountBinding>(),
//    ,DataListener,
//    Scanner.StatusListener,
    ZebraScanner.OnDataScanned,
    OnClickListener {

    companion object {
        fun newInstance() = CycleCountFragment()
    }
    private lateinit var scanner: ZebraScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCycleCountBinding
        get() = FragmentCycleCountBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner = ZebraScanner(requireActivity(),this)
        setUpOrganizationsSpinner()
        observeGettingOrganizations()
        setUpCycleTypeRadioGroup()
        observeGettingItemsList()
        setUpItemsSpinner()
        observeGettingLocatorsList()
        setUpLocatorsSpinner()
        observeCreatingCycleCount()
        Tools.clearInputLayoutError(binding.organizations,binding.itemCode,binding.locatorCode)
        attachButtonsToListener(this,binding.startCycleCount)
    }

    private fun observeCreatingCycleCount() {
        viewModel.createNewCycleCountStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.createNewCycleCountLiveData.observe(requireActivity()){
            val bundle = Bundle()
            bundle.putString(CYCLE_COUNT_HEADER_KEY,CycleCountHeader.toJson(it))
            Log.d(TAG, "observeCreatingCycleCount: ${it.id}")
            bundle.putString(ORGANIZATION_KEY, selectedOrganizationCode)
            if (binding.cycleCountType.checkedRadioButtonId==R.id.by_item) {
                bundle.putString(ITEM_KEY,Item.toJson(selectedItem!!))
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_cycleCountFragment_to_startCycleCountByItemFragment,
                    bundle
                )
            } else if (binding.cycleCountType.checkedRadioButtonId==R.id.by_locator) {
                bundle.putString(LOCATOR_KEY,LocatorAudit.toJson(selectedLocator!!))
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_cycleCountFragment_to_startCycleCountByLocatorFragment,
                    bundle
                )
            }
        }
    }

    private var selectedLocator:LocatorAudit? = null
    private fun setUpLocatorsSpinner() {
//        binding.locatorCodeSpinner.setOnItemClickListener { adapterView, view, i, l ->
//            selectedLocator = locatorsList[i]
//        }
    }
    private var locatorsList:List<LocatorAudit> = listOf()
    private lateinit var locatorsAdapter: ArrayAdapter<LocatorAudit>
    private fun observeGettingLocatorsList() {
        viewModel.getLocatorsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.getLocatorsListLiveData.observe(requireActivity()){
//            locatorsList = it
//            locatorsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,locatorsList)
//            binding.locatorCodeSpinner.setAdapter(locatorsAdapter)
            if (it.isNotEmpty()){
                selectedLocator = it[0]
                binding.locatorCode.editText?.setText(selectedLocator?.locatorCode!!)
            } else {
                binding.locatorCode.error = getString(R.string.wrong_locator)
            }
        }
    }
    private var selectedItem:Item? = null
    private fun setUpItemsSpinner() {
        binding.itemCodeSpinner.setOnItemClickListener { adapterView, view, i, l ->
            selectedItem = itemsList[i]
        }
    }
    private var itemsList:List<Item> = listOf()
    private lateinit var itemsListAdapter:ArrayAdapter<Item>
    private fun observeGettingItemsList() {
        viewModel.getItemsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    warningDialog(requireContext(),it.message)
                }
            }
        }
        viewModel.getItemsListLiveData.observe(requireActivity()){
            itemsList = it
            itemsListAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,itemsList)
            binding.itemCodeSpinner.setAdapter(itemsListAdapter)
        }
    }

    private fun setUpCycleTypeRadioGroup() {
        binding.cycleCountType.setOnCheckedChangeListener { _, selectedTypeId ->
            when (selectedTypeId) {
                R.id.by_item -> {
                    binding.locatorCode.visibility = GONE
                    binding.organizations.visibility = VISIBLE
                    binding.itemCode.visibility = VISIBLE
                    if (organizationsList.isEmpty())
                        viewModel.getOrganizationsList()
                }

                R.id.by_locator -> {
                    binding.locatorCode.visibility = VISIBLE
                    binding.organizations.visibility = VISIBLE
                    binding.itemCode.visibility = GONE
                    if (organizationsList.isEmpty())
                        viewModel.getOrganizationsList()
//                    if (locatorsList.isEmpty())
//                        viewModel.getLocatorsList()
                }
            }
        }
    }

    private var  organizationsList :List<OrganizationAudit> = listOf()
    private lateinit var organizationsAdapter :ArrayAdapter<OrganizationAudit>
    private fun observeGettingOrganizations() {
        viewModel.getOrganizationsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    showErrorAlerter(it.message,requireActivity())
                }
            }
        }
        viewModel.getOrganizationsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                organizationsList = it
                organizationsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,organizationsList)
                binding.organizationsSpinner.setAdapter(organizationsAdapter)
            } else {
                showErrorAlerter(getString(R.string.no_organizations_found), requireActivity())
            }
        }
    }

    private var selectedOrganizationCode:String? = null
    private fun setUpOrganizationsSpinner() {
        binding.organizationsSpinner.setOnItemClickListener { adapterView, view, i, l ->
            selectedOrganizationCode = organizationsList[i].orgCode
            if (binding.cycleCountType.checkedRadioButtonId == R.id.by_item)
                viewModel.getItemsList(selectedOrganizationCode!!)
//            else
//                viewModel.getLocatorsList(selectedOrganizationCode!!)
        }
    }

    private fun isReadyToStartCycleCount():Boolean{
        var isReady = true
        val selectedCycleCountType = binding.cycleCountType.checkedRadioButtonId
        if (selectedOrganizationCode==null) {
            binding.organizations.error = getString(R.string.please_select_organization)
            isReady = false
        }
        if (selectedItem==null&&selectedCycleCountType==R.id.by_item) {
            binding.itemCode.error = getString(R.string.please_scan_or_select_item_code)
            isReady = false
        }
        if (selectedLocator==null&&selectedCycleCountType==R.id.by_locator) {
            binding.locatorCode.error = getString(R.string.please_scan_or_select_locator_code)
            isReady = false
        }
        return isReady
    }

//    override fun onData(scanDataCollection: ScanDataCollection) {
//        requireActivity().runOnUiThread {
//            val scannedText = scanner.onData(scanDataCollection)
//            if (binding.itemCode.visibility== VISIBLE){
//                val item = itemsList.find { it.itemCode == scannedText }
//                if (item!=null){
//                    selectedItem = item
//                    binding.itemCodeSpinner.setText(item.itemDescription,false)
//                } else {
//                    binding.itemCode.error = getString(R.string.wrong_item_code)
//                }
//            } else if (binding.locatorCode.visibility == VISIBLE) {
////                val locator = locatorsList.find { it.locatorCode == scannedText }
////                if (locator!=null){
////                    selectedLocator = locator
////                    binding.locatorCodeSpinner.setText(locator.locatorCode,false)
////                } else {
////                    binding.locatorCode.error = getString(R.string.wrong_locator)
////                }
//                if (selectedOrganizationCode!=null){
//                    viewModel.getLocatorData(selectedOrganizationCode!!,scannedText)
//                } else {
//                    binding.organizations.error = getString(R.string.select_organization_first)
//                }
//            }
//            scanner.restartReadData()
//        }
//    }
//
//
//    override fun onStatus(p0: StatusData?) {
//        scanner.onStatus(p0)
//    }

    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.cycle_count),requireActivity())
        scanner.onResume()
        viewModel.getOrganizationsList()
        binding.organizationsSpinner.setText("")
        binding.itemCodeSpinner.setText("")
        binding.locatorCode.editText?.setText("")
    }

    override fun onPause() {
        super.onPause()
        scanner.onPause()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.start_cycle_count ->{
                val selectedCycleCountType = binding.cycleCountType.checkedRadioButtonId
                if (isReadyToStartCycleCount()){
                    if (selectedCycleCountType==R.id.by_item)
                        viewModel.createNewCycleCountListByItem(selectedItem?.itemCode!!,selectedOrganizationCode!!)
                    else if (selectedCycleCountType==R.id.by_locator)
                        viewModel.createNewCycleCountListByLocator(selectedLocator?.locatorId!!,selectedOrganizationCode!!)
                }
            }
        }
    }

    override fun onDataScanned(data: String) {
        val scannedText = data
        if (binding.itemCode.visibility== VISIBLE){
            val item = itemsList.find { it.itemCode == scannedText }
            if (item!=null){
                selectedItem = item
                binding.itemCodeSpinner.setText(item.itemDescription,false)
            } else {
                binding.itemCode.error = getString(R.string.wrong_item_code)
            }
        } else if (binding.locatorCode.visibility == VISIBLE) {
//                val locator = locatorsList.find { it.locatorCode == scannedText }
//                if (locator!=null){
//                    selectedLocator = locator
//                    binding.locatorCodeSpinner.setText(locator.locatorCode,false)
//                } else {
//                    binding.locatorCode.error = getString(R.string.wrong_locator)
//                }
            if (selectedOrganizationCode!=null){
                viewModel.getLocatorData(selectedOrganizationCode!!,scannedText)
            } else {
                binding.organizations.error = getString(R.string.select_organization_first)
            }
        }
    }

}