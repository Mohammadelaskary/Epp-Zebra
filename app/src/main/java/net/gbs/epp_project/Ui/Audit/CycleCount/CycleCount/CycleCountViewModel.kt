package net.gbs.epp_project.Ui.Audit.CycleCount.CycleCount

import android.app.Activity
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.CycleCountHeader
import net.gbs.epp_project.Model.Item
import net.gbs.epp_project.Model.LocatorAudit
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.OrganizationAudit
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.AuditRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent

class CycleCountViewModel(private val application: Application,activity: Activity) : BaseViewModel(application, activity) {
    val auditRepository = AuditRepository(activity)

    var orgCode:String? = null

    val getOrganizationsListLiveData = SingleLiveEvent<List<OrganizationAudit>>()
    val getOrganizationsListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getOrganizationsList(){
        getOrganizationsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = auditRepository.getOrganizations()
                ResponseDataHandler(response,getOrganizationsListLiveData,getOrganizationsListStatus,application).handleData()
            } catch (ex:Exception){
                getOrganizationsListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_connection)))
            }
        }
    }
    val getItemsListLiveData = SingleLiveEvent<List<Item>>()
    val getItemsListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getItemsList(orgCode:String){
        getItemsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = auditRepository.getAllItems(orgCode)
                ResponseDataHandler(response,getItemsListLiveData,getItemsListStatus,application).handleData()
            } catch (ex:Exception){
                getItemsListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_connection)))
            }
        }
    }
    val getLocatorsListLiveData = SingleLiveEvent<List<LocatorAudit>>()
    val getLocatorsListStatus = SingleLiveEvent<StatusWithMessage>()


    fun getLocatorData(orgCode:String,locatorCode:String){
        getLocatorsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = auditRepository.getLocatorData(orgCode,locatorCode)
                ResponseDataHandler(response,getLocatorsListLiveData,getLocatorsListStatus,application).handleData()
            } catch (ex:Exception){
                getLocatorsListStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_connection)))
            }
        }
    }

    val createNewCycleCountLiveData = SingleLiveEvent<CycleCountHeader>()
    val createNewCycleCountStatus = SingleLiveEvent<StatusWithMessage>()
    fun createNewCycleCountListByItem(itemCode:String,organizationCode:String){
        createNewCycleCountStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = auditRepository.createNewCycleCountOrderByItem(itemCode,organizationCode)
                ResponseDataHandler(response,createNewCycleCountLiveData,createNewCycleCountStatus,application).handleData()
            } catch (ex:Exception){
                createNewCycleCountStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_connection)))
            }
        }
    }

    fun createNewCycleCountListByLocator(locatorId:Int,orgCode: String){
        createNewCycleCountStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = auditRepository.createNewCycleCountOrderByLocator(locatorId,orgCode)
                ResponseDataHandler(response,createNewCycleCountLiveData,createNewCycleCountStatus,application).handleData()
            } catch (ex:Exception){
                createNewCycleCountStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_connection)))
            }
        }
    }


}