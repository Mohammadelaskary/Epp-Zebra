package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.MoveOrderLine

class MoveOrderLinesGetByHeaderIdResponse(
    @SerializedName("getList"        ) var getList        : ArrayList<MoveOrderLine> = arrayListOf()
): BaseResponse<List<MoveOrderLine>>() {
    override fun getData(): List<MoveOrderLine> {
        return getList
    }

}
