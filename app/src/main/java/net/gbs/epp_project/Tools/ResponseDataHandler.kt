package net.gbs.epp_project.Tools

import android.Manifest.permission
import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Model.StatusWithMessage
import net.gbs.epp_project.R
import retrofit2.Response
import java.io.File
import java.io.FileWriter

class ResponseDataHandler<T : BaseResponse<D>,D> (val response: Response<T>, private val responseData :SingleLiveEvent<D>, private val statusWithMessage: SingleLiveEvent<StatusWithMessage>, val application: Application){
    fun handleData() {
        try {
            if (response.isSuccessful) {
                if (response.body()?.responseStatus?.isSuccess!!) {
                    responseData.postValue(response.body()?.getData()!!)
                    statusWithMessage.postValue(
                        StatusWithMessage(
                            Status.SUCCESS,
                            response.body()?.responseStatus?.statusMessage!!
                        )
                    )
                } else {
                    statusWithMessage.postValue(
                        StatusWithMessage(
                            Status.ERROR,
                            response.body()?.responseStatus?.statusMessage!!
                        )
                    )
                }
            } else {
                statusWithMessage.postValue(
                    StatusWithMessage(
                        Status.NETWORK_FAIL,
                        application.getString(
                            R.string.error_in_getting_data
                        )
                    )
                )
            }

        } catch (ex: Exception) {
            statusWithMessage.postValue(
                StatusWithMessage(
                    Status.NETWORK_FAIL,
//                    application.getString(
//                        R.string.error_in_getting_data
//                    )
                    ex.message.toString()
                )
            )
            Log.e(TAG, "handleData: ", ex)
        }
    }

}