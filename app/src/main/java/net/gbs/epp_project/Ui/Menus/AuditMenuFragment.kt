package net.gbs.epp_project.Ui.Menus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.changeFragmentTitle
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import net.gbs.epp_project.databinding.FragmentAuditMenuBinding


class AuditMenuFragment : Fragment() {

    private lateinit var binding:FragmentAuditMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentAuditMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAuthority()
        binding.startAudit.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_auditMenuFragment_to_auditListFragment)
        }
        binding.finishAudit.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_auditMenuFragment_to_finishTrackingAuditListFragment)
        }
        binding.cycleCount.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_auditMenuFragment_to_cycleCountFragment)
        }
    }

    private fun handleAuthority() {
        binding.cycleCount.isEnabled = USER?.isCycleCount!!
        binding.startAudit.isEnabled = USER?.isPhysicalInventory!!
    }

    override fun onResume() {
        super.onResume()
        changeFragmentTitle(getString(R.string.audit_menu),requireActivity())
        showBackButton(requireActivity())
    }

}