package net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueMenus.FinalProductsMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.FINAL_PRODUCT
import net.gbs.epp_project.Base.BundleKeys.INDIRECT_CHEMICALS
import net.gbs.epp_project.Base.BundleKeys.ISSUE_FINAL_PRODUCT
import net.gbs.epp_project.Base.BundleKeys.ORGANIZATION_ID_KEY
import net.gbs.epp_project.Base.BundleKeys.RECEIVE_FINAL_PRODUCT
import net.gbs.epp_project.Base.BundleKeys.SOURCE_KEY
import net.gbs.epp_project.Base.BundleKeys.SPARE_PARTS
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.attachButtonsToListener
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Ui.Issue.EppOrganizations.IssueMenus.EppOrganizationsIssueViewModel
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import net.gbs.epp_project.databinding.FragmentEppOrganizationsFinalProductsMenuBinding
import net.gbs.epp_project.databinding.FragmentEppOrganizationsSparePartsIssueBinding

class EppOrganizationsFinalProductMenuFragment : BaseFragmentWithViewModel<EppOrganizationsIssueViewModel,FragmentEppOrganizationsFinalProductsMenuBinding>(),OnClickListener {

    companion object {
        fun newInstance() = EppOrganizationsFinalProductMenuFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEppOrganizationsFinalProductsMenuBinding
        get() = FragmentEppOrganizationsFinalProductsMenuBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpOrganizationsSpinner()
        observeGettingOrganizationsList()


        clearInputLayoutError(binding.organizations)
        attachButtonsToListener(this,binding.itemInfo,binding.moveOrderReceive,binding.issue)

        handleAuthority()
    }

    private fun handleAuthority() {
        binding.moveOrderReceive.isEnabled = USER?.isReceive!!
        binding.issue.isEnabled = USER?.isIssueFinalProduct!!
        binding.itemInfo.isEnabled = USER?.isItemInfo!!
    }

    private fun observeGettingOrganizationsList() {
        viewModel.getOrganizationsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> loadingDialog.hide()
                else -> {
                    warningDialog(requireContext(),it.message)
                    loadingDialog.hide()
                }
            }
        }
        viewModel.getOrganizationsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                organizations = it
                organizationsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,organizations)
                binding.organizationsSpinner.setAdapter(organizationsAdapter)
                setDefaultOrganizationFactory()
            } else {
                warningDialog(requireContext(),getString(R.string.no_organizations_found))
            }
        }
    }

    private fun setDefaultOrganizationFactory() {
        selectedOrgId = 84
        binding.organizationsSpinner.setText("EFO - EPP Factory Organization",false)
    }

    private lateinit var organizationsAdapter:ArrayAdapter<Organization>
    private var organizations = listOf<Organization>()
    private var selectedOrgId = -1
    private fun setUpOrganizationsSpinner() {
        binding.organizationsSpinner.setOnItemClickListener { _, _, i, _ ->
                selectedOrgId = organizations[i].organizationId!!
        }
    }

    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.final_products),requireActivity())
        showBackButton(requireActivity())
        viewModel.getOrganizationsList()
    }

    override fun onClick(v: View?) {
        if (selectedOrgId==-1){
            binding.organizations.error = getString(R.string.please_select_organization)
        } else {
            val bundle = Bundle()
            bundle.putInt(ORGANIZATION_ID_KEY,selectedOrgId)
            when(v?.id) {
                R.id.move_order_receive -> {
                    bundle.putString(SOURCE_KEY, RECEIVE_FINAL_PRODUCT)
                    Navigation.findNavController(v).navigate(
                        R.id.action_eppOrganizationsFinalProductMenuFragment_to_finishProductsTransactMoveOrderFragment,
                        bundle
                    )
                }
                R.id.issue -> {
                    bundle.putString(SOURCE_KEY, ISSUE_FINAL_PRODUCT)
                    Navigation.findNavController(v).navigate(
                        R.id.action_eppOrganizationsFinalProductMenuFragment_to_finishProductsTransactMoveOrderFragment,
                        bundle
                    )
                }
                R.id.item_info -> {
                    bundle.putString(SOURCE_KEY, FINAL_PRODUCT)
                    Navigation.findNavController(v).navigate(
                        R.id.action_eppOrganizationsFinalProductMenuFragment_to_finishedProductsItemInfoFragment,
                        bundle
                    )
                }
            }
        }
    }
}

