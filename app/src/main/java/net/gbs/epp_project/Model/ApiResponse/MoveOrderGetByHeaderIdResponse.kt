package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.MoveOrder

class MoveOrderGetByHeaderIdResponse(
    @SerializedName("moveOrder"      ) var moveOrder      : MoveOrder
):BaseResponse<MoveOrder>() {
    override fun getData(): MoveOrder {
        return moveOrder
    }

}
