package net.gbs.epp_project.Tools

import android.app.Application
import androidx.lifecycle.MutableLiveData
import net.gbs.epp_project.Model.Response.NoDataResponse
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.SingleLiveEvent
import retrofit2.Response

class ResponseHandler (val response: Response<NoDataResponse>, val statusWithMessage: SingleLiveEvent<StatusWithMessage>, val application: Application){
    fun handleData(){
        if (response.isSuccessful){
            if (response.body()?.responseStatus?.isSuccess!!){
                statusWithMessage.postValue(StatusWithMessage(Status.SUCCESS,response.body()?.responseStatus?.statusMessage!!))
            } else {
                statusWithMessage.postValue(StatusWithMessage(Status.ERROR,response.body()?.responseStatus?.statusMessage!!))
            }
        } else {
            statusWithMessage.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                R.string.error_in_getting_data)))
        }
    }
}