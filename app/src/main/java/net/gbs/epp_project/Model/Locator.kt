package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class Locator(
    @SerializedName("org_id"                  ) var orgId                   : Int?    = null,
    @SerializedName("locator_id"              ) var locatorId               : Int?    = null,
    @SerializedName("locatorCode"             ) var locatorCode             : String?    = null,
    @SerializedName("subinventorycode"        ) var subinventorycode        : String? = null,
    @SerializedName("subinventorydescription" ) var subinventorydescription : String? = null,
) {
    override fun toString(): String {
        return locatorCode!!
    }
}
