package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class Line(
    @SerializedName("line_id"                ) var lineId               : Int?    = null,
    @SerializedName("linE_NUMBER"            ) var lineNumber           : Int?    = null,
    @SerializedName("froM_SUBINVENTORY_CODE" ) var fromSubInventoryCode : String? = null,
    @SerializedName("froM_LOCATOR_CODE"      ) var fromLocatorCode      : String? = null,
    @SerializedName("tO_SUBINVENTORY_CODE"   ) var toSubInventoryCode   : String? = null,
    @SerializedName("tO_LOCATOR_CODE"        ) var toLocatorCode        : String? = null
)
