package net.gbs.epp_project.Ui.ItemInfo

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.OnHandItemForAllocate
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.SignInRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent

class ItemInfoViewModel(private val application: Application,activity: Activity) : BaseViewModel(application, activity) {
    private val repository = SignInRepository(activity)
    val getOrganizationsListLiveData = SingleLiveEvent<List<Organization>>()
    val getOrganizationsListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getOrganizationsList(){
        getOrganizationsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getOrganizations()
                ResponseDataHandler(response,getOrganizationsListLiveData,getOrganizationsListStatus,application).handleData()
            } catch (ex:Exception){
                getOrganizationsListStatus.postValue(
                    StatusWithMessage(
                        Status.NETWORK_FAIL,application.getString(
                            R.string.error_in_connection))
                )
            }
        }
    }

    val getItemsListLiveData = SingleLiveEvent<List<OnHandItemForAllocate>>()
    val getItemsListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getItemsList(orgId:Int,itemCode:String){
        getItemsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getItemInfo(itemCode,orgId)
                delay(200)
                ResponseDataHandler(response,getItemsListLiveData,getItemsListStatus,application).handleData()
            } catch (ex:Exception){
                getItemsListStatus.postValue(
                    StatusWithMessage(
                        Status.NETWORK_FAIL,application.getString(
                            R.string.error_in_connection))
                )
            }
        }
    }
}