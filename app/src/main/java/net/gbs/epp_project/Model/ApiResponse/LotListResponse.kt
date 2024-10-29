package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.Lot

class LotListResponse : BaseResponse<List<Lot>>() {
    @SerializedName("getList"        ) var getList        : List<Lot> = arrayListOf()
    override fun getData(): List<Lot> {
        return getList
    }

}
