package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class OnHandItemForAllocate (
    @SerializedName("orG_ID"               ) var orGID              : Int?    = null,
    @SerializedName("orG_CODE"             ) var orGCODE            : String? = null,
    @SerializedName("orG_NAME"             ) var orGNAME            : String? = null,
    @SerializedName("inventorY_ITEM_ID"    ) var inventorYITEMID    : Int?    = null,
    @SerializedName("iteM_CODE"            ) var iteMCODE           : String? = null,
    @SerializedName("iteM_DESCRIPTION"     ) var iteMDESCRIPTION    : String? = null,
    @SerializedName("maiN_CATEGORY"        ) var maiNCATEGORY       : String? = null,
    @SerializedName("category"             ) var category           : String? = null,
    @SerializedName("subinventory"         ) var subinventory       : String? = null,
    @SerializedName("subinventoryDesc"     ) var subinventoryDesc   : String? = null,
    @SerializedName("inventorY_LOCATOR_ID" ) var inventorYLOCATORID : Int?    = null,
    @SerializedName("locator"              ) var locator            : String? = null,
    @SerializedName("onhand"               ) var onhand             : Double?    = null,
    @SerializedName("uom"                  ) var uom                : String? = null,
    @SerializedName("availblE_QTY"         ) var availblEQTY        : Int?    = null
)