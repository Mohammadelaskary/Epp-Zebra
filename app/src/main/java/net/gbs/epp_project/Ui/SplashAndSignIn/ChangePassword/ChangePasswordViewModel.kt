package net.gbs.epp_project.Ui.SplashAndSignIn.ChangePassword

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Repositories.SignInRepository

class ChangePasswordViewModel(private val application: Application,activity: Activity) : BaseViewModel(application, activity) {
    val repository = SignInRepository(activity)
    fun changePassword(userName: String, currentPassword: String, newPassword: String, confirmPassword: String) {

    }


}