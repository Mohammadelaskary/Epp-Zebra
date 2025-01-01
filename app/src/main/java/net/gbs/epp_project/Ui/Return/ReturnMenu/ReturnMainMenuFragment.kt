package net.gbs.epp_project.Ui.Return.ReturnMenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.showBackButton
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment
import net.gbs.epp_project.databinding.FragmentReturnMainMenuBinding


class ReturnMainMenuFragment : Fragment() {


    private lateinit var binding: FragmentReturnMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReturnMainMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.returnToVendor.isEnabled = SignInFragment.USER?.isReturnToVendor!!
        binding.returnToWarehouse.isEnabled = SignInFragment.USER?.isReturnToWarehouse!!
        binding.returnToVendor.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_returnMainMenuFragment_to_returnToVendorFragment) }
        binding.returnToWarehouse.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_returnMainMenuFragment_to_returnMenuFragment) }
    }

    override fun onResume() {
        super.onResume()
        showBackButton(requireActivity())
        Tools.changeFragmentTitle(getString(R.string.return_menu), requireActivity())
    }
}