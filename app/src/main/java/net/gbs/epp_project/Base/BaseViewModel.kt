package net.gbs.epp_project.Base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Job
import net.gbs.epp_project.Tools.LocaleHelper
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment
import java.text.SimpleDateFormat
import java.util.Date

open class BaseViewModel (application: Application,activity: Activity): AndroidViewModel(application) {
    var job: Job? = null
    val lang = LocaleHelper.getLanguage(application.applicationContext)!!
    val deviceSerialNo = Settings.Secure.getString(
        application.contentResolver,
        Settings.Secure.ANDROID_ID
    )



//    val sharedPref: SharedPreferences =
//        application.getSharedPreferences(LOCATION_KEY, Context.MODE_PRIVATE)
//    fun savePlantToLocalStorage (plant: Plant){
//        sharedPref.edit().putString(PLANT_KEY,Plant.toJson(plant)).apply()
//    }
//    fun saveWarehouseToLocalStorage (warehouse: Warehouse){
//        sharedPref.edit().putString(WAREHOUSE_KEY,Warehouse.toJson(warehouse)).apply()
//    }
//    fun getPlantFromLocalStorage ():Plant? {
//        val plant = sharedPref.getString(PLANT_KEY,null)
//        return if (plant == null)
//            null
//        else
//            Plant.fromJson(plant)
//    }
//    fun getWarehouseFromLocalStorage ():Warehouse? {
//        val warehouse = sharedPref.getString(WAREHOUSE_KEY,null)
//        return if (warehouse == null)
//            null
//        else
//            Warehouse.fromJson(warehouse)
//    }
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun getDeviceTodayDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return sdf.format(Date())+".00"
    }
}