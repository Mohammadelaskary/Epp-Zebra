package net.gbs.epp_project.Ui.Transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.ORGANIZATION_ID_KEY
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.attachButtonsToListener
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import net.gbs.epp_project.databinding.FragmentEppOrganizationsTransferBinding

class EppOrganizationsTransferFragment : BaseFragmentWithViewModel<EppOrganizationsTransferViewModel,FragmentEppOrganizationsTransferBinding>(),OnClickListener {

    companion object {
        fun newInstance() = EppOrganizationsTransferFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEppOrganizationsTransferBinding
        get() = FragmentEppOrganizationsTransferBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpOrganizationsSpinner()
        observeGettingOrganizationsList()


        clearInputLayoutError(binding.organizations)
        attachButtonsToListener(this,binding.startTransfer)


    }

    private fun observeGettingOrganizationsList() {
        viewModel.getOrganizationsListStatus.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    warningDialog(requireContext(),it.message)
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
                warningDialog(requireContext(),getString(R.string.no_organizations_found))
            }
        }
    }

    private lateinit var organizationsAdapter:ArrayAdapter<Organization>
    private var organizations = listOf<Organization>()
    private var selectedOrgId = -1
    private var selectedOrgCode = ""
    private fun setUpOrganizationsSpinner() {
        binding.organizationsSpinner.setOnItemClickListener { _, _, i, _ ->
                selectedOrgId = organizations[i].organizationId!!
                selectedOrgCode = organizations[i].organizationCode!!
        }
    }

    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.transfer_menu),requireActivity())
        showBackButton(requireActivity())
        viewModel.getOrganizationsList()
    }

    override fun onClick(v: View?) {
        if (selectedOrgId==-1){
            binding.organizations.error = getString(R.string.please_select_organization)
        } else {
            val userOrganization = USER?.organizations?.find { it.orgId == selectedOrgId }
            if (userOrganization!=null) {
                val bundle = Bundle()
                bundle.putInt(ORGANIZATION_ID_KEY, selectedOrgId)
                when (v?.id) {
                    R.id.start_transfer -> {
                        Navigation.findNavController(v).navigate(
                            R.id.action_eppOrganizationsTransferFragment_to_startTransferFragment,
                            bundle
                        )
                    }
                }
            } else {
                warningDialog(requireContext(),getString(R.string.this_user_isn_t_authorized_to_select_that_organization))
            }
        }
    }
}

