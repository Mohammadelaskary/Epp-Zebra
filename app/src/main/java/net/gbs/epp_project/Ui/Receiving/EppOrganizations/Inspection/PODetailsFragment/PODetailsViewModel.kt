package net.gbs.epp_project.Ui.Receiving.EppOrganizations.Inspection.PODetailsFragment

import android.app.Activity
import android.app.Application

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.ApiRequestBody.MobileLogBody
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.OrganizationAudit
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.Repositories.ReceivingRepository
import net.gbs.epp_project.Tools.LoadingDialog
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Ui.Receiving.EppOrganizations.Receive.StartReceiving.PoDetailsAdapter
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER

class PODetailsViewModel(app: Application,val activity: Activity) : BaseViewModel(app,activity) {
    val receivingRepository = ReceivingRepository(activity)
    fun getPurchaseOrderDetailsList(
        loadingDialog: LoadingDialog,
        poAdapter: PoDetailsAdapter,
        orgId: Int, poHeaderId: String
    ) {
        loadingDialog!!.show()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.getPurchaseOrderDetailsList(orgId, poHeaderId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    poAdapter.poList = response.body()?.getData()!!
                    loadingDialog!!.hide()
                } else {
                    Tools.warningDialog(activity, response.body()?.responseStatus?.statusMessage!!)
                    loadingDialog!!.hide()
                }
            }
            if (response.body()?.responseStatus?.errorMessage!=null)
                receivingRepository.MobileLog(
                    MobileLogBody(
                        userId = USER?.notOracleUserId,
                        errorMessage = response.body()?.responseStatus?.errorMessage,
                        apiName = "PurchaseOrderLinesGetByID"
                    )
                )
        }
    }
    val getPoOrganizationsLiveData = SingleLiveEvent<List<Organization>>()
    val getPoOrganizationsStatus = SingleLiveEvent<StatusWithMessage>()
    fun getPoOrganizations(poHeaderId: String) {
        getPoOrganizationsStatus.postValue(StatusWithMessage(Status.LOADING))
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = receivingRepository.getPoOrganizations(poHeaderId)
            ResponseDataHandler(
                response = response,
                getPoOrganizationsLiveData,
                getPoOrganizationsStatus,
                getApplication()
            ).handleData("PurchaseOrderGetOrganizations")
            if (response.body()?.responseStatus?.errorMessage!=null)
                receivingRepository.MobileLog(
                    MobileLogBody(
                        userId = USER?.notOracleUserId,
                        errorMessage = response.body()?.responseStatus?.errorMessage,
                        apiName = "PurchaseOrderGetOrganizations"
                    )
                )
        }
    }
}