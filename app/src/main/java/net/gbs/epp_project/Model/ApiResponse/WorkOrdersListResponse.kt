package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.WorkOrderOrder

class WorkOrdersListResponse(
    @SerializedName("getList"        ) var getList        : ArrayList<WorkOrderOrder> = arrayListOf()

): BaseResponse<List<WorkOrderOrder>>() {
    override fun getData(): List<WorkOrderOrder> {
        return getList
    }

}
