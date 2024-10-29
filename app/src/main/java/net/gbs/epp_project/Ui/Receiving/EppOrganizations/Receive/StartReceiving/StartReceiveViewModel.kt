package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.StartReceiving

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.OrganizationAudit
import net.gbs.epp_project.Model.PODetailsItem
import net.gbs.epp_project.Model.PoLine
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.ReceivingRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.ResponseHandler
import net.gbs.epp_project.Tools.SingleLiveEvent


class StartReceiveViewModel (private val app: Application, val activity: Activity) : BaseViewModel(app,activity) {
    val getPoOrganizationsLiveData = SingleLiveEvent<List<Organization>>()
    val getPoOrganizationsStatus = SingleLiveEvent<StatusWithMessage>()
    val receivingRepository = ReceivingRepository(activity)
    fun getPoOrganizations(poHeaderId: String){
        getPoOrganizationsStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = receivingRepository.getPoOrganizations(poHeaderId)
                ResponseDataHandler(
                    response,
                    getPoOrganizationsLiveData,
                    getPoOrganizationsStatus,
                    getApplication()
                ).handleData()
            } catch (ex:Exception){
                getPoOrganizationsStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,app.getString(R.string.error_in_getting_data)))
            }
        }
    }

    val getNextReceiptNoLiveData = SingleLiveEvent<String>()
    val getNextReceiptNoStatus = SingleLiveEvent<StatusWithMessage>()
    fun getNextReceiptNo(orgId: Int){
        getNextReceiptNoStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                ResponseDataHandler(
                receivingRepository.getOrganizationsNextReceiptNo(orgId),
                getNextReceiptNoLiveData,
                getNextReceiptNoStatus,
                getApplication()
            ).handleData()
            } catch (ex:Exception){
                getNextReceiptNoStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,app.getString(
                    R.string.error_in_getting_data)))
            }
        }
    }

    val getPreviousReceiptNoLiveData = SingleLiveEvent<List<String>>()
    val getPreviousReceiptNoStatus = SingleLiveEvent<StatusWithMessage>()
    fun getPreviousReceiptNoList(orgId: Int,poHeaderId: String){
        getPreviousReceiptNoStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try{
            ResponseDataHandler(
                receivingRepository.getPreviousReceiptNoList(orgId, poHeaderId),
                getPreviousReceiptNoLiveData,
                getPreviousReceiptNoStatus,
                getApplication()
            ).handleData()
            } catch (ex:Exception){
                getPreviousReceiptNoStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,app.getString(R.string.error_in_getting_data)))
            }
        }
    }

    val getPurchaseOrderDetailsListLiveData = SingleLiveEvent<List<PODetailsItem>>()
    val getPurchaseOrderDetailsListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getPurchaseOrderDetailsList(
        orgId:Int, poHeaderId:String
    ) {
        getPurchaseOrderDetailsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try{
            val response = receivingRepository.getPurchaseOrderDetailsList(orgId, poHeaderId)
            ResponseDataHandler(
                response,
                getPurchaseOrderDetailsListLiveData,
                getPurchaseOrderDetailsListStatus,
                app
            ).handleData()
            } catch (ex:Exception){
                getPurchaseOrderDetailsListStatus.postValue(StatusWithMessage(Status.LOADING,app.getString(R.string.error_in_getting_data)))
            }
        }
    }
    val itemReceivingResultStatus = SingleLiveEvent<StatusWithMessage>()
    fun ItemsReceiving(
        poHeaderId: Int,
        poLines: List<PoLine>,
        transactionDate: String
    ) {
            itemReceivingResultStatus.postValue(StatusWithMessage(Status.LOADING))
            job = CoroutineScope(Dispatchers.IO).launch {
                try {
                val response =
                    receivingRepository.ItemReceiving(poHeaderId, poLines, transactionDate)
                ResponseHandler(response, itemReceivingResultStatus, getApplication()).handleData()
                } catch (ex:Exception){
                    Log.d(TAG, "ItemsReceiving: ${ex.message}")
                    itemReceivingResultStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,app.getString(R.string.error_in_getting_data)))
                }
            }
    }
//    val getDateLiveData = SingleLiveEvent<String>()
//    val getDateStatus   = SingleLiveEvent<StatusWithMessage>()
//    fun getTodayDate(){
//        getDateStatus.postValue(StatusWithMessage(Status.LOADING))
//        job = CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = receivingRepository.getDate()
//                ResponseDataHandler(response,getDateLiveData,getDateStatus,app).handleData()
//            } catch (ex:Exception){
//                getDateStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,app.getString(R.string.error_in_getting_data)))
//            }
//        }
//    }

    fun getTodayDate() = receivingRepository.getTodayDate()
    fun getDisplayTodayDate()= receivingRepository.getTodayDate().substring(0,10)
//    fun getTodayFullDate() = receivingRepository.getStoredActualFullDateTime()
}