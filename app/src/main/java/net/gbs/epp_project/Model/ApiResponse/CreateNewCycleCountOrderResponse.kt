package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.CycleCountHeader

class CreateNewCycleCountOrderResponse(
    @SerializedName("cycleCountHeader" ) var cycleCountHeader : CycleCountHeader
) :BaseResponse<CycleCountHeader>() {
    override fun getData(): CycleCountHeader {
        return cycleCountHeader
    }

}
