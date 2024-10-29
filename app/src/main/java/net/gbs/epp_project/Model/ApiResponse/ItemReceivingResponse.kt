package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.PODetailsItem

data class ItemReceivingResponse(
    @SerializedName("getPoDetails"   )val getPoDetails: PODetailsItem,
): BaseResponse<PODetailsItem>() {
    override fun getData() = getPoDetails
}