package net.gbs.epp_project.MainActivity

import android.app.Activity
import net.gbs.epp_project.Base.LocalStorage
import java.text.SimpleDateFormat
import java.util.Date

class MainRepository(activity: Activity) {
    val localStorage = LocalStorage(activity)
    fun getStoredDeviceDate()            = localStorage.getStoredDeviceDate()
    fun getStoredActualDate()            = localStorage.getStoredActualDate()
    fun setStoredDeviceDate(date:String) = localStorage.setStoredDeviceDate(date)
    fun setStoredActualDate(date:String) = localStorage.setStoredActualDate(date)

    fun getTodayDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }
}