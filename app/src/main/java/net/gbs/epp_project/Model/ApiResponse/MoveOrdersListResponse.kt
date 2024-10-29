package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.MoveOrder

class MoveOrdersListResponse(
    @SerializedName("getList"        ) var getList        : ArrayList<MoveOrder> = arrayListOf()

): BaseResponse<List<MoveOrder>>() {
    override fun getData(): List<MoveOrder> {
        return getList
    }

}
