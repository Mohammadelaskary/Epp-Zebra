package net.gbs.epp_project.Ui.Return.ReturnToWarehouse.StartReturn

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.databinding.FragmentStartReturnBinding

class StartReturnFragment : BaseFragmentWithViewModel<StartReturnViewModel, FragmentStartReturnBinding>() {

    companion object {
        fun newInstance() = StartReturnFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartReturnBinding
        get() = FragmentStartReturnBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.showCalendar.setOnClickListener {
            Tools.datePicker(requireContext(),binding.transactionDate).show(requireActivity().supportFragmentManager,"Select a date")
        }

    }


    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.start_return), requireActivity())
        Tools.showBackButton(requireActivity())
    }
}