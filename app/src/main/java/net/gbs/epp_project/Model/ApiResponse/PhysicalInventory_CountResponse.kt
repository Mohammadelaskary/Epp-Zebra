package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.AuditOrder

class PhysicalInventory_CountResponse(
    @SerializedName("getList"        ) var getList        : List<AuditOrder> = arrayListOf()
):BaseResponse<List<AuditOrder>>() {
    override fun getData(): List<AuditOrder> {
        return getList
    }

}
