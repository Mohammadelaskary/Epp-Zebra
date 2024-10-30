package net.gbs.epp_project.Base

import android.app.Activity
import android.content.ContentValues.TAG
import android.provider.Settings
import android.util.Log
import com.honeywell.aidc.BuildConfig
import net.gbs.epp_project.MainActivity.MainActivity.Companion.BASE_URL
import net.gbs.epp_project.MainActivity.MainActivity.Companion.setBaseUrl
import net.gbs.epp_project.Network.ApiFactory.ApiFactory
import net.gbs.epp_project.Network.ApiInterface.ApiInterface
import net.gbs.epp_project.Tools.CommunicationData
import net.gbs.epp_project.Tools.FormatDateTime
import net.gbs.epp_project.Tools.LocaleHelper
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment
import java.text.SimpleDateFormat
import java.util.Date

open class BaseRepository(val activity: Activity?) {
    var apiInterface  :ApiInterface
    var communicationData:CommunicationData
    init {
        communicationData = CommunicationData(activity!!)
        setBaseUrl(
            communicationData.getProtocol(),
            communicationData.getIpAddress(),
            communicationData.getPortNumber(),
        )
        Log.d(TAG, "hasInternetConnectionRepo: $BASE_URL")
        apiInterface = ApiFactory.getInstance()?.create(ApiInterface::class.java)!!
    }

    val localStorage = LocalStorage(activity!!)
    val lang = LocaleHelper.getLanguage(activity!!)!!
    val deviceSerialNo = Settings.Secure.getString(
        activity?.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    val userId = SignInFragment.USER?.userId
    suspend fun getSubInventoryList(
        orgId:Int
    ) = apiInterface.getSubInvList(
        orgId = orgId.toString()
    )


    suspend fun getLotList(orgId:String,itemId:Int?,subInventoryCode:String?) = apiInterface.getLotList(
        userId = userId!!,
        deviceSerialNo = deviceSerialNo,
        appLang = lang,
        orgId = orgId,
        itemId = itemId,
        SUBINVENTORY_CODE = subInventoryCode
    )
    suspend fun getDate() = apiInterface.getDate()

//    fun getStoredDeviceDate()            = localStorage.getStoredDeviceDate()
//    fun getStoredActualDate()            = localStorage.getStoredActualDate()
//    fun getStoredActualFullDateTime()    = localStorage.getStoredActualFullDateTime()
    fun setStoredDeviceDate(date:String) = localStorage.setStoredDeviceDate(date)
    fun setStoredActualDate(date:String) = localStorage.setStoredActualDate(date)

    fun getTodayDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = FormatDateTime(sdf.format(Date()))
        Log.d(TAG, "getTodayDate: ${date.year()}-${date.month()}-${date.day()} ${date.hours()}:${date.minutes()}:00")
        return "${date.year()}-${date.month()}-${date.day()}T${date.hours()}:${date.minutes()}:00.00"
    }
}