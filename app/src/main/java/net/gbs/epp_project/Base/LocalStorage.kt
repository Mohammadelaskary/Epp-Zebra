package net.gbs.epp_project.Base

import android.app.Activity
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences




class LocalStorage(activity: Activity) {
    private val FIRST_TIME_KEY = "firstTimeKey"
    private val STORED_DEVICE_DATE_KEY = "StoredDeviceDate"
    var sharedPreferences: SharedPreferences = activity.getPreferences(MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()
    val STORED_ACTUAL_DATE_KEY = "StoredActualDate"
    val STORED_ACTUAL_FULL_DATE_TIME_KEY = "StoredActualFullDateTime"
    fun getStoredDeviceDate():String?{
        return sharedPreferences.getString(STORED_DEVICE_DATE_KEY,"")
    }
    fun getStoredActualDate():String?{
        return sharedPreferences.getString(STORED_ACTUAL_DATE_KEY,"")
    }
    fun getStoredActualFullDateTime():String?{
        return sharedPreferences.getString(STORED_ACTUAL_FULL_DATE_TIME_KEY,"")
    }
    fun setStoredDeviceDate(date:String){
        editor.putString(STORED_DEVICE_DATE_KEY, date)
        editor.commit()
    }
    fun setStoredActualDate(date:String){
        editor.putString(STORED_ACTUAL_DATE_KEY, date)
        editor.commit()
    }
    fun setStoredActualFullDateTime(date:String){
        editor.putString(STORED_ACTUAL_FULL_DATE_TIME_KEY, date)
        editor.commit()
    }
    fun setFirstTime(firstTime:Boolean){
        editor.putBoolean(FIRST_TIME_KEY, firstTime)
        editor.commit()
    }
    fun getFirstTime():Boolean{
        return sharedPreferences.getBoolean(FIRST_TIME_KEY,true)
    }
}