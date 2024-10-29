package net.gbs.epp_project.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class WorkOrderOrder (
    @SerializedName("moveOrder_HEADER_ID"          ) var moveOrderHeaderId          : Int?    = null,
    @SerializedName("moveOrder_REQUEST_NUMBER"     ) var moveOrderRequestNumber     : String?    = null,
//    @SerializedName("moveOrder_DESCRIPTION"        ) var moveOrderDescription       : String? = null,
    @SerializedName("transactioN_TYPE_ID"          ) var transactionTypeId          : String? = null,
    @SerializedName("transactioN_TYPE_DESCRIPTION" ) var transactionTypeDescription : String? = null,
    @SerializedName("headeR_STATUS"                ) var headerStatus               : String? = null,
    @SerializedName("headeR_STATUS_DESCRIPTION"    ) var headerStatusDescription    : String? = null,
    @SerializedName("movE_ORDER_TYPE"              ) var moveOrderType              : String? = null,
    @SerializedName("movE_ORDER_DESCRIPTION"       ) var moveOrderDescription       : String? = null,
    @SerializedName("work_Order_Name"              ) var workOrderName              : String? = null,

) {
    override fun toString(): String {
            return workOrderName!!
    }
    companion object {
        fun toJson(moveOrder:WorkOrderOrder):String {
            return Gson().toJson(moveOrder)
        }
        fun fromJson(moveOrder:String):WorkOrderOrder {
            return Gson().fromJson(moveOrder,WorkOrderOrder::class.java)
        }
    }
}