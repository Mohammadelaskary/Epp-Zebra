package net.gbs.epp_project.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class POLineReturn(
    @SerializedName("transactioN_ID"          ) var transactioNID        : Int?            = null,
    @SerializedName("transactioN_TYPE"        ) var transactioNTYPE      : String?         = null,
    @SerializedName("ship_to_organization_id" ) var shipToOrganizationId : Int?            = null,
    @SerializedName("receipt_num"             ) var receiptNum           : String?            = null,
    @SerializedName("po_line_id"              ) var poLineId             : Int?            = null,
    @SerializedName("quantity_returned"       ) var quantityReturned     : Double?            = null,
    var itemDescription:String? = null,
    @SerializedName("lots"                    ) var lots                 : ArrayList<LotQty>? = arrayListOf()
) {
    override fun toString(): String {
        return "$itemDescription     $lots"
    }
}