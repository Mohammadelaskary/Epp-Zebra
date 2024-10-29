package net.gbs.epp_project.Model.Response

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.AuditOrder
import java.io.Serial

data class GetPhysicalInventoryOrderListResponse(
    @SerializedName("getList") val ordersList: List<AuditOrder>
): BaseResponse<List<AuditOrder>>() {
    override fun getData(): List<AuditOrder> {
        return ordersList
    }
}
