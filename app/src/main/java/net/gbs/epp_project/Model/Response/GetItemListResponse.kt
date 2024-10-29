package net.gbs.epp_project.Model.Response

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.Item

class GetItemListResponse (
    @SerializedName("getList") val itemsList:List<Item>
        ):BaseResponse<List<Item>>(){
    override fun getData(): List<Item> = itemsList
}
