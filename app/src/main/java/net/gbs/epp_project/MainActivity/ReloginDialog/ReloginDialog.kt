package net.gbs.epp_project.MainActivity.ReloginDialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.clearInputLayoutError
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.databinding.ReloginDialogLayoutBinding

class ReloginDialog(context: Context,private val onReloginDialogButtonsClicked: OnReloginDialogButtonsClicked):Dialog(context) {
    private lateinit var binding: ReloginDialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReloginDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clearInputLayoutError(binding.userName,binding.password)
        window?.decorView?.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        binding.logout.setOnClickListener {
            onReloginDialogButtonsClicked.OnLogOutButtonClicked()
        }
        binding.relogin.setOnClickListener {
            val userName = getEditTextText(binding.userName)
            val password = getEditTextText(binding.password)
            if (isReadyToSave(userName,password))
                onReloginDialogButtonsClicked.OnReloginButtonClicked(userName,password)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.password.editText?.setText("")
    }

    private fun isReadyToSave(userName: String, password: String): Boolean {
        var isReady = true
        if (userName.isEmpty()){
            isReady = false
            binding.userName.error = context.getString(R.string.please_enter_your_user_name)
        }
        if (password.isEmpty()){
            isReady = false
            binding.password.error = context.getString(R.string.please_enter_your_password)
        }
        return isReady
    }

    interface OnReloginDialogButtonsClicked {
        fun OnReloginButtonClicked(userName:String,password:String)
        fun OnLogOutButtonClicked()
    }
}