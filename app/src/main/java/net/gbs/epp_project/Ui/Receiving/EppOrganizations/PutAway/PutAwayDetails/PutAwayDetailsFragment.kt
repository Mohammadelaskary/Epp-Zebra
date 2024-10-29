package net.gbs.epp_project.Ui.Receiving.EppOrganizations.PutAway.PutAwayDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.databinding.FragmentPutAwayDetailsBinding

class PutAwayDetailsFragment : BaseFragmentWithViewModel<PutAwayDetailsViewModel,FragmentPutAwayDetailsBinding>() {

    companion object {
        fun newInstance() = PutAwayDetailsFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPutAwayDetailsBinding
        get() = FragmentPutAwayDetailsBinding::inflate

    override fun onResume() {
        super.onResume()
        Tools.changeFragmentTitle(getString(R.string.put_away_details), requireActivity())
    }
}