package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.OnHandItem

class OnHandListResponse(
    @SerializedName("getList"        ) var getList        : ArrayList<OnHandItem> = arrayListOf()
) : BaseResponse<List<OnHandItem>>() {
    override fun getData(): List<OnHandItem> {
        return getList
    }

}
