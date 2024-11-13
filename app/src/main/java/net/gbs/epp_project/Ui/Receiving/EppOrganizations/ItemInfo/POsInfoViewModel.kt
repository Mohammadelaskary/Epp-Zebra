package net.gbs.epp_project.Ui.Receiving.EppOrganizations.ItemInfo

import android.app.Activity
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.ReceivingRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER

class POsInfoViewModel(private val application: Application, activity: Activity) : BaseViewModel(application, activity) {
    val repository = ReceivingRepository(activity)
    val getItemInfoLiveData = SingleLiveEvent<List<PODetailsItem2>>()
    val getItemInfoStatus = SingleLiveEvent<StatusWithMessage>()
    fun getItemInfo(itemCode:String){
        getItemInfoStatus.postValue(StatusWithMessage(Status.LOADING))
        try {
            job = CoroutineScope(Dispatchers.IO).launch {
                val response = repository.getItemInfo(itemCode)
                ResponseDataHandler(
                    response,
                    getItemInfoLiveData,
                    getItemInfoStatus,
                    application
                ).handleData("PurchaseOrderReceiptNoList")
                if (response.body()?.responseStatus?.errorMessage!=null)
                    repository.MobileLog(
                        MobileLogBody(
                            userId = USER?.notOracleUserId,
                            errorMessage = response.body()?.responseStatus?.errorMessage,
                            apiName = "PurchaseOrderReceiptNoList"
                        )
                    )
            }
        } catch (ex:Exception){
            getItemInfoStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                R.string.error_in_connection)))
        }
    }

    fun getPoReceivedList(pono:String){
        getItemInfoStatus.postValue(StatusWithMessage(Status.LOADING))
        try {
            job = CoroutineScope(Dispatchers.IO).launch {
                val response = repository.getPoReceivedInfo(pono)
                ResponseDataHandler(
                    response,
                    getItemInfoLiveData,
                    getItemInfoStatus,
                    application
                ).handleData("PurchaseOrderReceiptNoList")
                if (response.body()?.responseStatus?.errorMessage!=null)
                    repository.MobileLog(
                        MobileLogBody(
                            userId = USER?.notOracleUserId,
                            errorMessage = response.body()?.responseStatus?.errorMessage,
                            apiName = "PurchaseOrderReceiptNoList"
                        )
                    )
            }
        } catch (ex:Exception){
            getItemInfoStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                R.string.error_in_connection)))
        }
    }
}