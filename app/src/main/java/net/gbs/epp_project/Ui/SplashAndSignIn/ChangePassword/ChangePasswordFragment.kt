package net.gbs.epp_project.Ui.SplashAndSignIn.ChangePassword

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import net.gbs.epp_project.Base.BaseFragmentWithViewModel
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : BaseFragmentWithViewModel<ChangePasswordViewModel,FragmentChangePasswordBinding>() {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChangePasswordBinding
        get() = FragmentChangePasswordBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changePassword.setOnClickListener {
            val userName = getEditTextText(binding.userName)
            val currentPassword = getEditTextText(binding.currentPassword)
            val newPassword = getEditTextText(binding.newPassword)
            val confirmPassword = getEditTextText(binding.confirmPassword)
            if (isReadyToSave(userName,currentPassword,newPassword,confirmPassword)){
                viewModel.changePassword(userName,currentPassword,newPassword,confirmPassword)
            }
        }
    }

    private fun isReadyToSave(
        userName: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        var isReady = true
        if (userName.isEmpty()){
            isReady = false
            binding.userName.error = getString(R.string.please_enter_your_user_name)
        }
        if (currentPassword.isEmpty()){
            isReady = false
            binding.currentPassword.error = getString(R.string.please_enter_current_password)
        }
        if (newPassword.isEmpty()){
            isReady = false
            binding.newPassword.error = getString(R.string.please_enter_new_password)
        }
        if (confirmPassword.isEmpty()){
            isReady = false
            binding.confirmPassword.error = getString(R.string.please_enter_password_again)
        } else {
            if (confirmPassword != newPassword){
                isReady = false
                binding.confirmPassword.error = getString(R.string.password_don_t_match)
                binding.newPassword.error = getString(R.string.password_don_t_match)
            }
        }
        return isReady
    }
}