package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.DeliverLot
import net.gbs.epp_project.Model.Lot

class DeliverLotListResponse : BaseResponse<List<DeliverLot>>() {
    @SerializedName("getList"        ) var getList        : List<DeliverLot> = arrayListOf()
    override fun getData(): List<DeliverLot> {
        return getList
    }

}
