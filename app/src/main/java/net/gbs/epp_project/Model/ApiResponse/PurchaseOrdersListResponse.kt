package net.gbs.epp_project.Model.ApiResponse


import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.PurchaseOrder

data class PurchaseOrdersListResponse(
    @SerializedName("getList")
    val poList: List<PurchaseOrder>
    ) : BaseResponse<List<PurchaseOrder>>() {
    override fun getData(): List<PurchaseOrder> = poList

}