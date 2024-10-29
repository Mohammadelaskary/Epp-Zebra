package net.gbs.epp_project.Ui.Audit.AuditedList

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.AuditTransaction
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.AuditRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import java.lang.Exception

class AuditedListViewModel(private val application: Application,val activity: Activity) : BaseViewModel(application,activity) {
    private val auditRepository = AuditRepository(activity)
    val getAuditTransactionsListLiveData = SingleLiveEvent<List<AuditTransaction>>()
    val getAuditTransactionsListStatus   = SingleLiveEvent<StatusWithMessage>()
    fun getTransactionsList(headerId:Int){
        getAuditTransactionsListStatus.postValue(StatusWithMessage(Status.LOADING))
        try {
            job = CoroutineScope(Dispatchers.IO).launch {
                try{
                    val response = auditRepository.getTransactionsList(headerId)
                    ResponseDataHandler(
                        response,
                        getAuditTransactionsListLiveData,
                        getAuditTransactionsListStatus,
                        application
                    ).handleData()
                } catch (ex:Exception){
                    Log.e(TAG, "getTransactionsList: ", ex)
                    getAuditTransactionsListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                        R.string.error_in_getting_data)))
                }
            }
        } catch (ex:Exception){
            Log.e(TAG, "getTransactionsList: ", ex)
            getAuditTransactionsListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                R.string.error_in_getting_data)))
        }
    }
}