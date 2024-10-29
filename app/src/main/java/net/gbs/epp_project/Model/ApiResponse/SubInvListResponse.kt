package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.SubInventory

class SubInvListResponse(
    @SerializedName("getList") val subInventoryList : List<SubInventory>
): BaseResponse<List<SubInventory>>() {
    override fun getData(): List<SubInventory> {
        return subInventoryList
    }

}
