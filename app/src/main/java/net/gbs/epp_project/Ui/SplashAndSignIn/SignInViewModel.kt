package net.gbs.epp_project.Ui.SplashAndSignIn

import android.app.Activity
import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.ApiRequestBody.SignInBody
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.Model.User
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.SignInRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import java.net.SocketTimeoutException

class SignInViewModel(private val application: Application,val activity: Activity) : BaseViewModel(application = application,activity) {
    private var repository = SignInRepository(activity)
    val signInLiveData = SingleLiveEvent<User>()
    val signInStatus   = SingleLiveEvent<StatusWithMessage>()

    fun refreshRepository(){
        repository = SignInRepository(activity)
    }
    fun signIn(userName: String, password: String) {
        signInStatus.postValue(StatusWithMessage(Status.LOADING))
        try {
            job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = repository.signIn(
                        SignInBody(
                        userName,password
                    )
                    )
                    ResponseDataHandler(response,signInLiveData,signInStatus,application).handleData("SignIn")
                    if (response.body()?.responseStatus?.errorMessage!=null)
                        repository.MobileLog(
                            MobileLogBody(
                                errorMessage = response.body()?.responseStatus?.errorMessage,
                                apiName = "SignIn"
                            )
                        )
                } catch (ex: Exception){
                    Log.e("SignInViewModel", "signIn: ", ex)
                    signInStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(R.string.error_in_sending_data)))
                }

            }
        } catch (ex: SocketTimeoutException){
            Log.e("SignInViewModel", "signIn: ", ex)
            signInStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(R.string.error_in_sending_data)))
        }
    }

}