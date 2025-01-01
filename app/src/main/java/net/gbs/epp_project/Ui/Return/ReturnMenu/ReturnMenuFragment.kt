package net.gbs.epp_project.Ui.Return.ReturnMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.ORGANIZATION_ID_KEY
import net.gbs.epp_project.Base.BundleKeys.RETURN_TO_WAREHOUSE
import net.gbs.epp_project.Base.BundleKeys.RETURN_TO_WIP
import net.gbs.epp_project.Base.BundleKeys.SOURCE_KEY
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import net.gbs.epp_project.databinding.FragmentReturnMenuBinding

class ReturnMenuFragment : BaseFragmentWithViewModel<ReturnMenuViewModel,FragmentReturnMenuBinding>() {

    companion object {
        fun newInstance() = ReturnMenuFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReturnMenuBinding
        get() = FragmentReturnMenuBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAuthority()

        binding.returnToWarehouse.setOnClickListener {
            if (selectedOrgId != -1) {
                val userOrganization = USER?.organizations?.find { it.orgId == selectedOrgId }
                if (userOrganization!=null) {
                    val bundle = Bundle()
                    bundle.putInt(ORGANIZATION_ID_KEY, selectedOrgId)
                    bundle.putString(SOURCE_KEY, RETURN_TO_WAREHOUSE)
                    Navigation.findNavController(it)
                        .navigate(
                            R.id.action_returnMenuFragment_to_returnToWarehouseFragment,
                            bundle
                        )
                } else {
                    warningDialog(requireContext(),getString(R.string.this_user_isn_t_authorized_to_select_that_organization))
                }
            } else
                binding.organizations.error = getString(R.string.please_select_organization)
        }
        binding.returnFromWipToProduction.setOnClickListener {
            if (selectedOrgId != -1) {
                val userOrganization = USER?.organizations?.find { it.orgId == selectedOrgId }
                if (userOrganization!=null) {
                    val bundle = Bundle()
                    bundle.putInt(ORGANIZATION_ID_KEY, selectedOrgId)
                    bundle.putString(SOURCE_KEY, RETURN_TO_WIP)
                    Navigation.findNavController(it)
                        .navigate(R.id.action_returnMenuFragment_to_returnToWipFragment, bundle)
                } else {
                    warningDialog(requireContext(),getString(R.string.this_user_isn_t_authorized_to_select_that_organization))
                }
            } else
                binding.organizations.error = getString(R.string.please_select_organization)
        }
        setUpOrganizationsSpinner()
        observeGettingOrganizationsList()


        Tools.clearInputLayoutError(binding.organizations)


    }

    private fun handleAuthority() {
        binding.returnToWarehouse.isEnabled = USER?.isReturnToWarehouse!!
        binding.returnFromWipToProduction.isEnabled = USER?.isReturnToWarehouse!!
    }

    private fun observeGettingOrganizationsList() {
        viewModel.getOrganizationsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    Tools.warningDialog(requireContext(), it.message)
                    loadingDialog!!.hide()
                }
            }
        }
        viewModel.getOrganizationsListLiveData.observe(requireActivity()){
            if (it.isNotEmpty()){
                organizations = it
                organizationsAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,organizations)
                binding.organizationsSpinner.setAdapter(organizationsAdapter)
                
            } else {
                Tools.warningDialog(requireContext(), getString(R.string.no_organizations_found))
            }
        }
    }

    private lateinit var organizationsAdapter: ArrayAdapter<Organization>
    private var organizations = listOf<Organization>()
    private var selectedOrgId = -1
    private fun setUpOrganizationsSpinner() {
        binding.organizationsSpinner.setOnItemClickListener { _, _, i, _ ->
            selectedOrgId = organizations[i].organizationId!!
        }
    }


    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.start_return),requireActivity())
        Tools.showBackButton(requireActivity())
        viewModel.getOrganizationsList()
        selectedOrgId = -1
        binding.organizationsSpinner.setText("")
    }
}