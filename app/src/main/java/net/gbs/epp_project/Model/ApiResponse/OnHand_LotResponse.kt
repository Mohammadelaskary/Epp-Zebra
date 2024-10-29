package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.OnHandItemLot

class OnHandLotResponse(
    @SerializedName("getList"        ) var getList        : List<OnHandItemLot> = arrayListOf()
):BaseResponse<List<OnHandItemLot>>() {
    override fun getData(): List<OnHandItemLot> {
        return getList
    }
}
