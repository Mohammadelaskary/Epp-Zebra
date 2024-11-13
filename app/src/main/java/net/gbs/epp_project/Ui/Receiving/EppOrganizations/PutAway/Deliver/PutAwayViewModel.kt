package net.gbs.epp_project.Ui.Receiving.EppOrganizations.PutAway.Deliver

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.PODetailsItem2
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Repositories.ReceivingRepository
import net.gbs.epp_project.Tools.LoadingDialog
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER

class PutAwayViewModel(private val app:Application,val activity: Activity) : BaseViewModel(application = app,activity) {
   val receivingRepository = ReceivingRepository(activity)
    var orgNum:String? = null
    var poNum:String? = null
    var receiptNo:String? = null
    val poDetailsItemsLiveData = SingleLiveEvent<List<PODetailsItem2>>()
    val poDetailsItemsStatus = SingleLiveEvent<StatusWithMessage>()
    fun getPurchaseOrderReceiptNoList(
        poNum: String,
        receiptNo: String,
    ) {
        poDetailsItemsStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = receivingRepository.getPurchaseOrderReceiptNoList(poNum, receiptNo)
                ResponseDataHandler(response,poDetailsItemsLiveData,poDetailsItemsStatus,app).handleData("PurchaseOrderReceiptNoList")
                if (response.body()?.responseStatus?.errorMessage!=null)
                    receivingRepository.MobileLog(
                        MobileLogBody(
                            userId = USER?.notOracleUserId,
                            errorMessage = response.body()?.responseStatus?.errorMessage,
                            apiName = "PurchaseOrderReceiptNoList"
                        )
                    )
            } catch (ex:Exception){
                poDetailsItemsStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,activity.getString(R.string.error_in_connection)))
            }
        }
    }
}