package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.ReceivePO

import android.app.Activity
import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.PurchaseOrder
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.ReceivingRepository
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER

class ReceivePOViewModel(private val application: Application,activity: Activity) :
    BaseViewModel(application,activity) {

    var poNumber :String?=null
    private val receivingRepository = ReceivingRepository(activity)
    val purchaseOrderLiveData = SingleLiveEvent<List<PurchaseOrder>>()
    val purchaseOrderStatus = SingleLiveEvent<StatusWithMessage> ()

     fun getPurchaseOrdersList(
         poNum: String,
     ) {
         purchaseOrderStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = receivingRepository.getPurchaseOrderByPoNo(poNum)
                ResponseDataHandler(
                    response,
                    purchaseOrderLiveData,
                    purchaseOrderStatus,
                    getApplication()
                ).handleData("PurchaseOrderGetByPoNo")
                if (response.body()?.responseStatus?.errorMessage!=null)
                    receivingRepository.MobileLog(
                        MobileLogBody(
                            userId = USER?.notOracleUserId,
                            errorMessage = response.body()?.responseStatus?.errorMessage,
                            apiName = "PurchaseOrderGetByPoNo"
                        )
                    )
            } catch (ex:Exception){
                purchaseOrderStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(
                    R.string.error_in_getting_data)))
            }
        }
    }
}