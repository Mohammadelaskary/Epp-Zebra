package net.gbs.epp_project.Ui.Audit.AuditList

import android.app.Activity
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.AuditOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.AuditRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import java.lang.Exception

class AuditListViewModel(private val app: Application,val activity: Activity) : BaseViewModel(app,activity) {
    val auditRepository = AuditRepository(activity)
    val getAuditOrdersListLiveData = SingleLiveEvent<List<AuditOrder>>()
    val getAuditOrdersListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getOrdersList(){
        getAuditOrdersListStatus.postValue(StatusWithMessage(Status.LOADING))
        try {
            job = CoroutineScope(Dispatchers.IO).launch {
                try{
                    val response = auditRepository.getAuditOrdersList()
                    ResponseDataHandler(
                        response,
                        getAuditOrdersListLiveData,
                        getAuditOrdersListStatus,
                        app
                    ).handleData("GetPhysicalInventoryOrderList")
                    if (response.body()?.responseStatus?.errorMessage!=null)
                        auditRepository.MobileLog(
                            MobileLogBody(
                                userId = USER?.notOracleUserId,
                                errorMessage = response.body()?.responseStatus?.errorMessage,
                                apiName = "GetPhysicalInventoryOrderList"
                            )
                        )
                } catch (ex:Exception){
                    getAuditOrdersListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,app.getString(R.string.error_in_getting_data)))
                }
            }
        } catch (ex:Exception){
            getAuditOrdersListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,app.getString(R.string.error_in_getting_data)))
        }
    }
}