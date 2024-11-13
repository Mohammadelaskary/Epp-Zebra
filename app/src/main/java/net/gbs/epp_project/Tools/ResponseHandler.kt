package net.gbs.epp_project.Tools

import android.app.Application
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseRepository
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.Response.NoDataResponse
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.Network.ApiFactory.ApiFactory
import net.gbs.epp_project.Network.ApiInterface.ApiInterface
import net.gbs.epp_project.R
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import retrofit2.Response

class ResponseHandler (val response: Response<NoDataResponse>, val statusWithMessage: SingleLiveEvent<StatusWithMessage>, val application: Application){

    fun handleData(apiName:String){
        if (response.isSuccessful){
            if (response.body()?.responseStatus?.isSuccess!!){
                statusWithMessage.postValue(StatusWithMessage(Status.SUCCESS,response.body()?.responseStatus?.statusMessage!!))
            } else {
                if (USER?.isShowErrorMessage!!&&response.body()?.responseStatus?.errorMessage!=null) {
                    statusWithMessage.postValue(
                        StatusWithMessage(
                            Status.ERROR,
                            response.body()?.responseStatus?.errorMessage!!
                        )
                    )
                } else{
                    statusWithMessage.postValue(
                        StatusWithMessage(
                            Status.ERROR,
                            response.body()?.responseStatus?.statusMessage!!
                        )
                    )
                }
            }
        } else {
            statusWithMessage.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                R.string.error_in_getting_data)))
        }
    }
}