package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class ReturnWorkOrder (
    @SerializedName("moveOrder_HEADER_ID"          ) var moveOrderHEADERID          : Int?    = null,
    @SerializedName("moveOrder_REQUEST_NUMBER"     ) var moveOrderREQUESTNUMBER     : String? = null,
    @SerializedName("moveOrder_DESCRIPTION"        ) var moveOrderDESCRIPTION       : String? = null,
    @SerializedName("transactioN_TYPE_ID"          ) var transactioNTYPEID          : String? = null,
    @SerializedName("transactioN_TYPE_DESCRIPTION" ) var transactioNTYPEDESCRIPTION : String? = null,
    @SerializedName("headeR_STATUS"                ) var headeRSTATUS               : String? = null,
    @SerializedName("headeR_STATUS_DESCRIPTION"    ) var headeRSTATUSDESCRIPTION    : String? = null,
    @SerializedName("movE_ORDER_TYPE"              ) var movEORDERTYPE              : String? = null,
    @SerializedName("movE_ORDER_DESCRIPTION"       ) var movEORDERDESCRIPTION       : String? = null,
    @SerializedName("work_Order_Name"              ) var workOrderName              : String? = null
){
    override fun toString(): String {
        return "$workOrderName"
    }
}
