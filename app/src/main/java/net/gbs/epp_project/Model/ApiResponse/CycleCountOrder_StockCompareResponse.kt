package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.ItemCompare

class CycleCountOrder_StockCompareResponse (
    @SerializedName("getList"        ) var getList        : ArrayList<ItemCompare> = arrayListOf()
): BaseResponse<List<ItemCompare>>() {
    override fun getData(): List<ItemCompare> {
        return getList
    }

}
