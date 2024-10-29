package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

class Lot (
    @SerializedName("org_id"               ) var orgId               : Int?    = null,
    @SerializedName("lotName"              ) var lotName             : String? = null,
    @SerializedName("inventorY_ITEM_ID"    ) var inventorYITEMID     : Int?    = null,
    @SerializedName("subinventorY_CODE"    ) var subinventorYCODE    : String? = null,
    @SerializedName("locatoR_ID"           ) var locatoRID           : Int?    = null,
    @SerializedName("transactioN_QUANTITY" ) var transactioNQUANTITY : Double? = null
) {
    override fun toString(): String {
        return "$lotName - Available ($transactioNQUANTITY)"
    }
}
