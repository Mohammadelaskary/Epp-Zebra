package net.gbs.epp_project.Model.Response

import com.google.gson.annotations.SerializedName

class NoDataResponse {
    @SerializedName("responseStatus" ) var responseStatus : ResponseStatus? = null
}