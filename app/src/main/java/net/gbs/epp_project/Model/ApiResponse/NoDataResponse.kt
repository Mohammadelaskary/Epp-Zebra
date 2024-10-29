package net.gbs.epp_project.Model.ApiResponse

import com.example.mobco.Model.ApiResponse.ResponseStatusX
import com.google.gson.annotations.SerializedName

class NoDataResponse {
    @SerializedName("responseStatus" ) var responseStatus : ResponseStatusX? = ResponseStatusX()
}