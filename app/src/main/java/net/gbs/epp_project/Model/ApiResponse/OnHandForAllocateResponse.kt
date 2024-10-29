package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.OnHandItemForAllocate

class OnHandForAllocateResponse (
    @SerializedName("getList"        ) var getList        : ArrayList<OnHandItemForAllocate> = arrayListOf()
) : BaseResponse<List<OnHandItemForAllocate>>() {
    override fun getData(): List<OnHandItemForAllocate> {
        return getList
    }
}
