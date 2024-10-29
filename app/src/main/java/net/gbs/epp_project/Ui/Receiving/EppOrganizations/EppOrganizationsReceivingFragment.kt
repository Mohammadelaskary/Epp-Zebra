package net.gbs.epp_project.Ui.Receiving.EppOrganizations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.Base.BundleKeys.PUT_AWAY_REJECT
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.changeTitle
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import net.gbs.epp_project.databinding.FragmentEppOrganizationsReceivingBinding

class EppOrganizationsReceivingFragment : BaseFragmentWithViewModel<EppOrganizationsReceivingViewModel, FragmentEppOrganizationsReceivingBinding>() {

    companion object {
        fun newInstance() = EppOrganizationsReceivingFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEppOrganizationsReceivingBinding
        get() = FragmentEppOrganizationsReceivingBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAuthority()
        binding.receiving.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_eppOrganizationsReceivingFragment_to_receivePOFragment) }
        binding.inspection.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_eppOrganizationsReceivingFragment_to_inspectionFragment) }
        val bundle = Bundle()
        binding.putAway.setOnClickListener {
            bundle.putBoolean(PUT_AWAY_REJECT,false)
            Navigation.findNavController(it).navigate(R.id.action_eppOrganizationsReceivingFragment_to_putAwayFragment,bundle) }
        binding.putAwayRejected.setOnClickListener {
            bundle.putBoolean(PUT_AWAY_REJECT,true)
            Navigation.findNavController(it).navigate(R.id.action_eppOrganizationsReceivingFragment_to_rejectionPutAwayFragment,bundle) }
        binding.itemInfo.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_eppOrganizationsReceivingFragment_to_itemInfoFragment) }

    }

    private fun handleAuthority() {
        binding.receiving.isEnabled = USER?.isReceive!!
        binding.inspection.isEnabled = USER?.isInspection!!
        binding.putAway.isEnabled = USER?.isDeliver!!
        binding.putAwayRejected.isEnabled = USER?.isDeliverRejected!!
        binding.itemInfo.isEnabled = USER?.isItemPos!!
    }

    override fun onResume() {
        super.onResume()
        showBackButton(requireActivity())
        changeFragmentTitle(getString(R.string.receiving_menu),requireActivity())
    }

}