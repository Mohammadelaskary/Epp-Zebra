package net.gbs.epp_project.MainActivity

import android.app.Activity
import net.gbs.epp_project.Base.BaseRepository
import net.gbs.epp_project.Model.ApiRequestBody.SignInBody


class MainRepository(activity: Activity):BaseRepository(activity){
    fun getStoredDeviceDate()            = localStorage.getStoredDeviceDate()
    fun getStoredActualDate()            = localStorage.getStoredActualDate()

    suspend fun signIn(body: SignInBody) = apiInterface.signIn(body)


}