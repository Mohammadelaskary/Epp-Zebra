package net.gbs.epp_project.Model.ApiResponse

import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import java.util.Calendar

class GetDateResponse(
    @SerializedName("currentDate") val currentDate :String
): BaseResponse<String>() {
    override fun getData(): String {
        return currentDate
    }
    fun timeFinished():Boolean {
        val currentCalendar = Calendar.getInstance()
        val dueDate     = Calendar.getInstance()
        currentCalendar[Calendar.YEAR] = currentDate.substring(0,4).toInt()
        currentCalendar[Calendar.MONTH] = currentDate.substring(5,7).toInt()
        currentCalendar[Calendar.DAY_OF_MONTH] = currentDate.substring(8,10).toInt()
        dueDate[Calendar.YEAR] = 2024
        dueDate[Calendar.MONTH] = 2
        dueDate[Calendar.DAY_OF_MONTH] = 8
        return dueDate < currentCalendar
    }
}
