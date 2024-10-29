package net.gbs.epp_project.Base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Model.Response.ResponseStatus


abstract class BaseResponse<T> {
    @SerializedName("responseStatus")
    @Expose
    var responseStatus: ResponseStatus? = null

    abstract fun getData(): T
}