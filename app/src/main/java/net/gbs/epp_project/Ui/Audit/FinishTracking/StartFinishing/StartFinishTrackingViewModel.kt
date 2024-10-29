package net.gbs.epp_project.Ui.Audit.FinishTracking.StartFinishing

import android.app.Activity
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.AuditRepository
import net.gbs.epp_project.Tools.ResponseHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import java.lang.Exception

class StartFinishTrackingViewModel(private val application: Application,val activity: Activity) : BaseViewModel(application,activity) {
    val auditRepository = AuditRepository(activity)
    val finishTrackingStatus = SingleLiveEvent<StatusWithMessage>()
    fun finishTracking(headerId:Int,subInventoryCode:String){
        finishTrackingStatus.postValue(StatusWithMessage(Status.LOADING))
        try {
            job = CoroutineScope(Dispatchers.IO).launch {
                try{
                    val response = auditRepository.finishTracking(subInventoryCode,headerId)
                    ResponseHandler(response,finishTrackingStatus,application).handleData()
                } catch (ex:Exception){
                    finishTrackingStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                        R.string.error_in_sending_data)))
                }
            }
        } catch (ex:Exception){
            finishTrackingStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                R.string.error_in_sending_data)))
        }
    }
}