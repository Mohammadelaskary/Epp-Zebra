package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.ReturnWorkOrder

class WorkOrderList_ReturnMaterialToInventoryResponse (
    @SerializedName("getList"        ) var getList        : ArrayList<ReturnWorkOrder> = arrayListOf()
            ):BaseResponse<List<ReturnWorkOrder>>() {
    override fun getData(): List<ReturnWorkOrder> {
        return getList
    }

}
