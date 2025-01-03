package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.POItem

data class PurchaseOrderItemList_ReturnResponse (
    @SerializedName("getList"        ) var itemList        : ArrayList<POItem> = arrayListOf()
) :BaseResponse<ArrayList<POItem>>(){
    override fun getData(): ArrayList<POItem> {
       return itemList
    }
}
