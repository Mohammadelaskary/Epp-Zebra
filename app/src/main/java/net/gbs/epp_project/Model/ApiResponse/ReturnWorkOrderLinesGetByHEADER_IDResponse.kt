package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.ReturnWorkOrderLine

class ReturnWorkOrderLinesGetByHEADER_IDResponse(
    @SerializedName("getList"        ) var getList        : ArrayList<ReturnWorkOrderLine> = arrayListOf()
):BaseResponse<List<ReturnWorkOrderLine>>() {
    override fun getData(): List<ReturnWorkOrderLine> {
        return  getList
    }

}
