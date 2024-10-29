package net.gbs.epp_project.Repositories

import android.app.Activity
import net.gbs.epp_project.Base.BaseRepository
import net.gbs.epp_project.Model.ApiRequestBody.SignInBody

class SignInRepository(activity: Activity):BaseRepository(activity) {
    suspend fun signIn(body: SignInBody) = apiInterface.signIn(body)
    suspend fun getOrganizations() =
        apiInterface.getOrganizationsList(
            userId!!,
            deviceSerialNo,
            lang,
            )

    suspend fun getItemInfo(itemCode:String,orgId:Int) =
        apiInterface.getOnHangItemInfo(userId = userId!!,
            DeviceSerialNo = deviceSerialNo,
            appLang = lang,
            itemCode = itemCode,
            orgId = orgId,
        )
}