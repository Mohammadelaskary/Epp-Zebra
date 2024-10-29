package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class POLineReturn(
    @SerializedName("transactioN_ID"          ) var transactioNID        : Int?    = null,
    @SerializedName("transactioN_TYPE"        ) var transactioNTYPE      : String? = null,
    @SerializedName("ship_to_organization_id" ) var shipToOrganizationId : Int?    = null,
    @SerializedName("receipt_num"             ) var receiptNum           : String?    = null,
    @SerializedName("po_line_id"              ) var poLineId             : Int?    = null,
    @SerializedName("quantity_returned"       ) var quantityReturned     : Int?    = null
)
