package net.gbs.epp_project.Ui.Audit.FinishTracking.FinishTrackingAuditList

import android.app.Activity
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.AuditOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.AuditRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import java.lang.Exception

class FinishTrackingAuditListViewModel(private val application: Application,val activity: Activity) : BaseViewModel(application,activity) {
    val auditRepository = AuditRepository(activity)
    val getAuditOrdersListLiveData = SingleLiveEvent<List<AuditOrder>>()
    val getAuditOrdersListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getOrdersList(){
        getAuditOrdersListStatus.postValue(StatusWithMessage(Status.LOADING))
        try {
            job = CoroutineScope(Dispatchers.IO).launch {
                try{
                    ResponseDataHandler(
                        auditRepository.getAuditOrdersList(),
                        getAuditOrdersListLiveData,
                        getAuditOrdersListStatus,
                        application
                    ).handleData()
                } catch (ex:Exception){
                    getAuditOrdersListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(R.string.error_in_getting_data)))
                }
            }
        } catch (ex:Exception){
            getAuditOrdersListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(R.string.error_in_getting_data)))
        }
    }
}