package net.gbs.epp_project.Ui.ContainersReceiving.CustomerDataSearch

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.gbs.epp_project.Base.BaseViewModel
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage

class CustomerNameSearchViewModel(private var activity: Activity,private var application: Application) : BaseViewModel(application, activity) {
    val getTrucksLiveData = MutableLiveData<List<Truck>>()
    val getTrucksStatus= MutableLiveData<StatusWithMessage>()
    fun getTrucks(){
        getTrucksLiveData.postValue(
            listOf(
                Truck("Truck1", listOf(Trailer("Trailer1","container1, container2"), Trailer("Trailer2","container3, container4")))
            )
        )
    }
}