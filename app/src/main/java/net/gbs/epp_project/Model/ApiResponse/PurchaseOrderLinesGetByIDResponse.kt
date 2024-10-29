package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.PODetailsItem

data class PurchaseOrderLinesGetByIDResponse(
    @SerializedName("getList")
    val getList: List<PODetailsItem>
) : BaseResponse<List<PODetailsItem>>() {
    override fun getData(): List<PODetailsItem> = getList
}