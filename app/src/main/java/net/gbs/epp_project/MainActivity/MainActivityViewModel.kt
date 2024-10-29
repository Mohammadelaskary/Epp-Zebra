package net.gbs.epp_project.MainActivity

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.gbs.epp_project.Base.BaseRepository
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.ResponseDataHandler
import net.gbs.epp_project.Tools.SingleLiveEvent

class MainActivityViewModel(private val application: Application,activity: Activity):AndroidViewModel(application) {
    val repository = MainRepository(activity)
//    var job: Job? = null
//    val getDateLiveData = SingleLiveEvent<String>()
//    val getDateStatus   = SingleLiveEvent<StatusWithMessage>()
//    private fun getActualTodayDate(){
//        getDateStatus.postValue(StatusWithMessage(Status.LOADING))
//        job = CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = repository.getDate()
//                ResponseDataHandler(response,getDateLiveData,getDateStatus,application).handleData()
//                repository.setStoredActualDate(response.body()?.currentDate!!.substring(0,10))
//                repository.setStoredDeviceDate(repository.getTodayDate().substring(0,10))
//            } catch (ex:Exception){
//                getDateStatus.postValue(StatusWithMessage(Status.NETWORK_FAIL,application.getString(R.string.error_in_getting_data)))
//            }
//        }
//    }
//

    fun getStoredDeviceDate() = repository.getStoredDeviceDate()
    fun getStoredActualDate() = repository.getStoredActualDate()
    fun getTodayDate()       = repository.getTodayDate()
}