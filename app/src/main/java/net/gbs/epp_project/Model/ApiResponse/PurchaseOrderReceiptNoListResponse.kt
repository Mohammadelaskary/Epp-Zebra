package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.PODetailsItem2

data class PurchaseOrderReceiptNoListResponse(
    @SerializedName("getList")
    val poList: List<PODetailsItem2>
) : BaseResponse<List<PODetailsItem2>>() {
    override fun getData(): List<PODetailsItem2> = poList
}