package net.gbs.epp_project.Ui.FinishedProductsItemInfo

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.OnHandItemForAllocate
import net.gbs.epp_project.Model.OnHandItemLot
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.IssueRepository
import net.gbs.epp_project.Repositories.SignInRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent

class FinishedProductsItemInfoViewModel(private val application: Application, activity: Activity) : BaseViewModel(application, activity) {
    private val repository = IssueRepository(activity)


    val getItemsListLiveData = SingleLiveEvent<List<OnHandItemLot>>()
    val getItemsListStatus = SingleLiveEvent<StatusWithMessage>()
    fun getItemsList(orgId:Int,itemCode:String){
        getItemsListStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getItemLotInfo(itemCode,orgId)
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