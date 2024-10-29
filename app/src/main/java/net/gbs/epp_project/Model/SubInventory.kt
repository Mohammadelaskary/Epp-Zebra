package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class SubInventory(
    @SerializedName("org_id", alternate = ["orgId"]                  ) var orgId                   : Int?    = null,
    @SerializedName("subinventorycode", alternate = ["subInventoryCode"]        ) var subInventoryCode        : String? = null,
    @SerializedName("subinventorydescription", alternate = ["subInventoryDesc"] ) var subinventorydescription : String? = null
) {
    override fun toString(): String {
        return subInventoryCode!!
    }
}
